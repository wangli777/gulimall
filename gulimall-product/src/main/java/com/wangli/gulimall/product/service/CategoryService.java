package com.wangli.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangli.common.utils.PageUtils;
import com.wangli.gulimall.product.entity.CategoryEntity;
import com.wangli.gulimall.product.vo.web.Catalog2Vo;

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

    /**
     * 批量删除
     * @param ids
     */
    void removeMenusByIds(List<Long> ids);

    /**
     * 找到catelogId的完整路径
     *
     * @param catelogId
     * @return
     */
    Long[] findCatelogPath(Long catelogId);

    /**
     * 级联更新所有关联的数据
     * @param category
     */
    void updateCascade(CategoryEntity category);

    /**
     * 查询一级分类
     * @return
     */
    List<CategoryEntity> listLevel1Cates();

    /**
     * 按首页分类要求查出 二级、三级分类数据
     * @return
     */
    Map<String, List<Catalog2Vo>> getCatalogJson();

}

