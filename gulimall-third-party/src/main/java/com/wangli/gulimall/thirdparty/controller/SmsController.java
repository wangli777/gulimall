package com.wangli.gulimall.thirdparty.controller;

import com.wangli.common.utils.R;
import com.wangli.gulimall.thirdparty.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangLi
 * @date 2021/7/17 15:41
 * @description
 */
@RestController
@RequestMapping("/sms")
public class SmsController {
    @Autowired
    SmsService smsService;

    /**
     * 提供给其他模块进行调用
     * @param phone
     * @param code
     * @return
     */
    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        smsService.sendSms(phone, code);
        return R.ok();
    }

}
