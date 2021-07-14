package com.wangli.gulimall.product.vo.web;

import com.wangli.gulimall.product.vo.spusave.Attr;
import lombok.Data;

import java.util.List;

/**
 * @author WangLi
 * @date 2021/7/14 22:04
 * @description
 */
@Data
public class SpuItemAttrGroupVo {

    private String groupName;

    private List<Attr> attrs;

}
