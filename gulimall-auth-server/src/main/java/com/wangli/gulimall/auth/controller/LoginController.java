package com.wangli.gulimall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.wangli.common.constant.AuthServerConstant;
import com.wangli.common.exception.BizCodeEnum;
import com.wangli.common.utils.R;
import com.wangli.gulimall.auth.feign.MemberFeignService;
import com.wangli.gulimall.auth.feign.ThirdPartyFeignService;
import com.wangli.gulimall.auth.vo.UserLoginVo;
import com.wangli.gulimall.auth.vo.UserRegisterVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author WangLi
 * @date 2021/7/16 21:53
 * @description 登录注册
 */
@Controller
public class LoginController {

    @Autowired
    ThirdPartyFeignService thirdPartyFeignService;

    @Autowired
    MemberFeignService memberFeignService;

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
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(), BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }

        //2、验证码的再次效验 redis.存key-phone,value-code
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        String codeNum = String.valueOf(code);
        //拼上当前时间，用于接口防刷
        String value = codeNum + "_" + System.currentTimeMillis();

        //存入redis，10分钟过期
        redisTemplate.opsForValue().set(redisKey, value, 10, TimeUnit.MINUTES);

        //调用短信服务发送验证码短信
        thirdPartyFeignService.sendCode(phone, codeNum);

        return R.ok();
    }

    /**
     * @param registerVo
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterVo registerVo, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            //如果有错误，获取错误信息，添加到RedirectAttributes中，

            Map<String, String> errorMap = new HashMap<>();
            //获取错误结果集
            bindingResult.getFieldErrors().forEach(e -> {
                //获取错误字段
                String field = e.getField();
                //获取错误信息
                String defaultMessage = e.getDefaultMessage();
//                if (!errorMap.containsKey(field)) {
                errorMap.put(field, defaultMessage);
//                }
            });
            redirectAttributes.addFlashAttribute("errors", errorMap);
            // 这里为什么用重定向而不是转发？ 这是因为前端请求注册接口是POST方式，使用转发方式转发到注册页，
            // 会使用原先的请求方式POST，而转发到页面只能使用GET，不支持POST，会报不支持POST的错误
            // 重定向想要携带数据，需要使用RedirectAttributes，其原理是使用了session，
            // 跳转到下一个页面取出数据后，session里的该数据就会删除。
            return "redirect:http://auth.mall.com/reg.html";
        }

        //校验验证码
        String code = registerVo.getCode();
        String phone = AuthServerConstant.SMS_CODE_CACHE_PREFIX + registerVo.getPhone();
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (StringUtils.isNotBlank(redisCode)) {
            if (code.equals(redisCode.split("_")[0])) {
                //校验通过
                //删除redis中的验证码
                redisTemplate.delete(phone);
                //调用远程服务进行注册
                R result = memberFeignService.register(registerVo);
                if (result.getCode() == 0) {
                    return "redirect:http://auth.mall.com/login.html";
                }else {
                    //失败
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", result.getData("msg",new TypeReference<String>(){}));
                    redirectAttributes.addFlashAttribute("errors",errors);
                    return "redirect:http://auth.mall.com/reg.html";
                }

            }else {
                //效验出错回到注册页面
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误");
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.mall.com/reg.html";
            }

        } else {
            //效验出错回到注册页面
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.mall.com/reg.html";
        }
    }

    @PostMapping("/login")
    public String login(UserLoginVo loginVo, RedirectAttributes redirectAttributes) {
        //调用远程登录服务
        R result = memberFeignService.login(loginVo);

        if (result.getCode() == 0) {
            //登录成功
            return "redirect:http://mall.com";

        }else{
            //登录失败
            HashMap<Object, Object> errors = Maps.newHashMap();
            errors.put("msg",result.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.mall.com/login.html";

        }
    }

}
