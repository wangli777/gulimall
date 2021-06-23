package com.wangli.gulimall.product.web;

import com.wangli.gulimall.product.entity.CategoryEntity;
import com.wangli.gulimall.product.service.CategoryService;
import com.wangli.gulimall.product.vo.web.Catalog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author WangLi
 * @date 2021/6/23 20:04
 * @description
 */
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @GetMapping({"/", "/index", "/index.html"})
    public String index(Model model) {
        //查出所有的一级分类
        List<CategoryEntity> list = categoryService.listLevel1Cates();
        model.addAttribute("categories", list);
        return "index";
    }

    //    index/catalog.json
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catalog2Vo>> getCatalogJson() {
        return categoryService.getCatalogJson();
    }
}
