package com.wangli.gulimall.product.feign;

import com.wangli.common.to.es.SkuEsModel;
import com.wangli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author WangLi
 * @date 2021/6/22 21:04
 * @description
 */
@FeignClient("gulimall-search")

public interface SearchFeignService {
    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
