package com.wangli.gulimall.search.feign;

import com.wangli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author WangLi
 * @date 2021/7/11 19:51
 * @description
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {
    @GetMapping("/product/attr/info/{attrId}")
    R attrInfo(@PathVariable("attrId") Long attrId);

}
