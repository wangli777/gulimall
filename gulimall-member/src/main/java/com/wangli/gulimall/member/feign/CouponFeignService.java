package com.wangli.gulimall.member.feign;

import com.wangli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author WangLi
 * @date 2021/5/30 15:54
 * @description @FeignClient("gulimall-coupon") 的值是 服务名
 * 从 gulimall-coupon 服务中，调用coupon/coupon/member/list接口
 */
@FeignClient(name = "gulimall-coupon", url = "http://localhost:7000/")
public interface CouponFeignService {

    @RequestMapping("coupon/coupon/member/list")
    R memberCoupons();
}
