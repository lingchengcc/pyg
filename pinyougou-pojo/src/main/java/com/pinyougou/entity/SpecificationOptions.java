package com.pinyougou.entity;

import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author silent
 * @version 1.0
 * @date 2019/6/26 9:41
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationOptions implements Serializable {
    private Specification specification;
    private List<SpecificationOption> optionList;
}
