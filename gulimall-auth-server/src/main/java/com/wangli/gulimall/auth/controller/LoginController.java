package com.wangli.gulimall.auth.controller;

import com.wangli.common.constant.AuthServerConstant;
import com.wangli.common.exception.BizCodeEnum;
import com.wangli.common.utils.R;
import com.wangli.gulimall.auth.feign.ThirdPartyFeignService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

/**
 * @author WangLi
 * @date 2021/7/16 21:53
 * @description
 */
@Controller
public class LoginController {

    @Autowired
    ThirdPartyFeignService thirdPartyFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/sms/sendCode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone) {
        //先看是否已经发送过，redis中是否已经存在数据了
        String redisKey = AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone;
        String redisValue = redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(redisValue)) {
            //发送过短信，并且是60s内重复请求，提示错误
            long time = Long.parseLong(redisValue.split("_")[1]);
            if (System.currentTimeMillis() - time < 60 * 1000) {
                //60秒内重复请求，
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(),BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }

        //2、验证码的再次效验 redis.存key-phone,value-code
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        String codeNum = String.valueOf(code);
        //拼上当前时间，用于接口防刷
        String value = codeNum + "_" + System.currentTimeMillis();

        //存入redis，10分钟过期
        redisTemplate.opsForValue().set(redisKey, value,10, TimeUnit.MINUTES);

        //调用短信服务发送验证码短信
        thirdPartyFeignService.sendCode(phone, codeNum);

        return R.ok();
    }
}
