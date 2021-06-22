package com.wangli.gulimall.search.controller;

import com.wangli.common.exception.BizCodeEnum;
import com.wangli.common.to.es.SkuEsModel;
import com.wangli.common.utils.R;
import com.wangli.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author WangLi
 * @date 2021/6/22 20:36
 * @description
 */
@RequestMapping("/search/save")
@RestController
@Slf4j
public class ElasticSaveController {
    @Autowired
    ProductSaveService productSaveService;

    /**
     * 上架商品，存es
     *
     * @param skuEsModels
     * @return
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean success = false;
        try {
            success = productSaveService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            log.error("ElasticSaveController 商品上架错误:{}", e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if (success) {

            return R.ok();
        } else {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }

}
