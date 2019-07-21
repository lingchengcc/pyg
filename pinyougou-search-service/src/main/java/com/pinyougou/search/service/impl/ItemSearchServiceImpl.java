package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.common.entity.SysConstants;
import com.pinyougou.pojo.Item;
import com.pinyougou.search.dao.ItemDao;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/4 17:10
 **/
@Service
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ItemDao itemDao;

    /**
     * 查询品牌和规格列表
     *
     * @param category 分类名称
     * @return
     */
    private Map<String, Object> searchBrandAndSpecList(String category) {
        Map<String, Object> map = new HashMap<>(3);
        //获取模板ID
        Long typeId = (Long) redisTemplate.boundHashOps(SysConstants.ITEM_CAT_REDIS_KEY).get(category);
        if (typeId != null) {
            //根据模板ID查询品牌列表
            List brandList = (List) redisTemplate.boundHashOps(SysConstants.BRAND_REDIS_KEY).get(typeId);
            //返回值添加品牌列表
            map.put("brandList", brandList);
            //根据模板ID查询规格列表
            List specList = (List) redisTemplate.boundHashOps(SysConstants.SPECIFICATION_REDIS_KEY).get(typeId);
            map.put("specList", specList);
        }
        return map;
    }

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {

        Map<String, Object> resultMap = new HashMap<>(6);
        //1.获取关键字
        String keywords = (String) searchMap.get("keywords");

        //2.创建搜索查询对象 的构建对象
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();

        //3.创建并添加查询条件 匹配查询
        searchQueryBuilder.withQuery(QueryBuilders
                .multiMatchQuery(keywords, "seller", "category", "brand", "title"));
        searchQueryBuilder.addAggregation(AggregationBuilders.terms("category_group").field("category").size(50));
        //3.1 设置高亮显示的域（字段） 设置 前缀 和后缀
        searchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("title"))
                .withHighlightBuilder(new HighlightBuilder()
                        .preTags("<em style=\"color:red\">")
                        .postTags("</em>"));
        //3.2 过滤查询  ----商品分类的过滤查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        String category = (String) searchMap.get("category");
        if (StringUtils.isNotBlank(category)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("category", category));
        }

        //3.3 过滤查询 ----商品的品牌的过滤查询
        String brand = (String) searchMap.get("brand");
        if (StringUtils.isNotBlank(brand)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("brand", brand));
        }
        Map<String, String> spec = (Map<String, String>) searchMap.get("spec");

        if (spec != null) {
            for (String key : spec.keySet()) {
                //该路径上去查询
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("specMap." + key + ".keyword", spec.get(key));
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }
        //4.过滤查询  价格区间过滤
        String price = (String) searchMap.get("price");

        if (StringUtils.isNotBlank(price)) {
            String[] split = price.split("-");
            if ("*".equals(split[1])) {
                //价格大于
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            } else {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(split[0], true).to(split[1], true));
            }
        }

        searchQueryBuilder.withFilter(boolQueryBuilder);

        //4.构建查询对象
        NativeSearchQuery searchQuery = searchQueryBuilder.build();

        //分页条件设置
        Integer pageNo = (Integer) searchMap.get("pageNo");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if (pageNo != null) {
            pageNo = 1;
        }
        if (pageSize != null) {
            pageSize = 40;
        }
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        searchQuery.setPageable(pageRequest);
        //设置排序
        //排序的字段
        String sortField = (String) searchMap.get("sortField");
        //排序的类型 DESC  ASC
        String sortType = (String) searchMap.get("sortType");
        if (StringUtils.isNotBlank(sortField) && StringUtils.isNotBlank(sortType)) {
            if (sortType.equals("ASC")) {
                Sort sort = new Sort(Sort.Direction.ASC, sortField);
                searchQuery.addSort(sort);
            } else if (sortType.equals("DESC")) {
                Sort sort = new Sort(Sort.Direction.DESC, sortField);
                searchQuery.addSort(sort);
            }
        }
        //5.执行查询
        AggregatedPage<Item> items = elasticsearchTemplate.queryForPage(searchQuery, Item.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                SearchHits hits = response.getHits();
                ArrayList<T> content = new ArrayList<>();
                //如果没有记录
                if (hits == null || hits.getHits().length <= 0) {
                    return new AggregatedPageImpl<>(content);
                }
                for (SearchHit hit : hits) {
                    String sourceAsString = hit.getSourceAsString();
                    //就是每一个文档对应的json数据
                    Item item = JSON.parseObject(sourceAsString, Item.class);
                    //获取高亮
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    //获取高亮的域为title的高亮对象
                    HighlightField title = highlightFields.get("title");
                    if (title != null) {
                        //获取高亮的碎片
                        Text[] fragments = title.getFragments();
                        //高亮的数据
                        StringBuilder buffer = new StringBuilder();
                        if (fragments != null) {
                            for (Text fragment : fragments) {
                                //获取到的高亮碎片的值<em styple="colore:red">
                                buffer.append(fragment.string());
                            }
                        }
                        //不为空的时候 存储值
                        if (StringUtils.isNotBlank(buffer.toString())) {
                            item.setTitle(buffer.toString());
                        }
                    }
                    content.add((T) item);
                }
                return new AggregatedPageImpl<>(content, pageable, hits.getTotalHits(), response.getAggregations(), response.getScrollId());
            }
        });
        //获取分组结果
        Aggregation categoryGroup = items.getAggregation("category_group");
        StringTerms terms = (StringTerms) categoryGroup;
        ArrayList<String> categoryList = new ArrayList<>();
        if (terms != null) {
            for (StringTerms.Bucket bucket : terms.getBuckets()) {
                categoryList.add(bucket.getKeyAsString());
            }
        }
        if (categoryList.size() != 0) {

            if (StringUtils.isNotBlank(category)) {
                Map<String, Object> map = searchBrandAndSpecList(category);
                resultMap.putAll(map);
            } else {
                Map<String, Object> map = searchBrandAndSpecList(categoryList.get(0));
                resultMap.putAll(map);
            }
            resultMap.put("categoryList", categoryList);
        }
        resultMap.put("total", items.getTotalElements());
        resultMap.put("rows", items.getContent());
        resultMap.put("totalPages", items.getTotalPages());
        return resultMap;
    }

    @Override
    public void updateIndex(List<Item> itemList) {
        for (Item item : itemList) {
            String spec = item.getSpec();
            item.setSpecMap(JSON.parseObject(spec, Map.class));
        }
        itemDao.saveAll(itemList);
    }

    @Override
    public void deleteByIds(Long[] ids) {
        DeleteQuery query = new DeleteQuery();
        query.setQuery(QueryBuilders.termsQuery("goodsId",ids));
        elasticsearchTemplate.delete(query,Item.class);
    }
}