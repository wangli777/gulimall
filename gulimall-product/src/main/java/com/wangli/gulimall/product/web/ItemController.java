package com.wangli.gulimall.product.web;

import com.wangli.gulimall.product.service.SkuInfoService;
import com.wangli.gulimall.product.vo.web.SkuItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * @author WangLi
 * @date 2021/7/14 21:06
 * @description 商品详情页面
 */
@Controller
@Slf4j
public class ItemController {

    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 展示当前sku的详情
     *
     * @param skuId
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String itemDesc(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {
        log.debug("准备查询{}的详情", skuId);

        SkuItemVo item = skuInfoService.item(skuId);

        model.addAttribute("item", item);

        return "item";
    }
}
