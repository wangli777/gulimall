package com.wangli.gulimall.search.controller;

import com.wangli.gulimall.search.service.MallSearchService;
import com.wangli.gulimall.search.vo.SearchParam;
import com.wangli.gulimall.search.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author WangLi
 * @date 2021/7/10 13:10
 * @description
 */
@Controller
public class SearchController {

    @Resource
    MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam, Model model) {
        SearchResult result = mallSearchService.search(searchParam);

        model.addAttribute("result", result);
        return "list";
    }
}
