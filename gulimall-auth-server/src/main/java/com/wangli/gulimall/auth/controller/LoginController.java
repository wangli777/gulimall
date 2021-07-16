package com.wangli.gulimall.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author WangLi
 * @date 2021/7/16 21:53
 * @description
 */
@Controller
public class LoginController {
    @GetMapping({"login", "login.html"})
    public String loginPage() {
        return "login";
    }
    @GetMapping({"reg", "reg.html"})
    public String regPage() {
        return "reg";
    }

}
