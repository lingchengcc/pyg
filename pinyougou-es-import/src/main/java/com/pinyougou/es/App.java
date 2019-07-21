package com.pinyougou.es;

import com.pinyougou.es.service.ItemService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/4 16:44
 **/
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-*.xml");
        ItemService itemService = context.getBean(ItemService.class);
        itemService.ImportDataToEs();
    }
}
