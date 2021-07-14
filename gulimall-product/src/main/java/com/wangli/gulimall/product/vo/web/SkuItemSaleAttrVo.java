package com.wangli.gulimall.product.vo.web;

import lombok.Data;

import java.util.List;

/**
 * @author WangLi
 * @date 2021/7/14 22:03
 * @description
 */
@Data
public class SkuItemSaleAttrVo {

    private Long attrId;

    private String attrName;

    private List<AttrValueWithSkuIdVo> attrValues;

}
