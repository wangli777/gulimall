package com.wangli.gulimall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author WangLi
 * @date 2021/7/27 20:55
 * @description
 */
@Controller
public class HelloController {

    @GetMapping("/{page}.html")
    public String goPage(@PathVariable("page") String page) {
        return page;
    }
}
