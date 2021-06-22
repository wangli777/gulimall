package com.wangli.gulimall.product.feign;

import com.wangli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author WangLi
 * @date 2021/6/22 20:10
 * @description 库存模块远程服务
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {
    @PostMapping(value = "/ware/waresku/hasStock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);
}
