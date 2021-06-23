package com.wangli.gulimall.product.app;

import com.wangli.common.utils.R;
import com.wangli.gulimall.product.entity.CategoryEntity;
import com.wangli.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 商品三级分类
 *
 * @author wangli
 * @email 1151723225@qq.com
 * @date 2021-05-30 13:00:10
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list/tree")
    public R list(@RequestParam Map<String, Object> params) {
//        PageUtils page = categoryService.queryPage(params);

        List<CategoryEntity> datas = categoryService.listWithTree();
        return R.ok().put("data", datas);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateCascade(category);
        return R.ok();
    }

    /**
     * 修改排序后的数据
     */
    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody CategoryEntity[] categorys) {
        categoryService.updateBatchById(Arrays.asList(categorys));

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
//		categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeMenusByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
