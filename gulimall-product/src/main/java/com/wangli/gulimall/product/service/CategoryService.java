package com.wangli.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangli.common.utils.PageUtils;
import com.wangli.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author wangli
 * @email 1151723225@qq.com
 * @date 2021-05-30 12:31:04
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查找所有商品目录分类，父子结构
     *
     * @return
     */
    List<CategoryEntity> listWithTree();
}

