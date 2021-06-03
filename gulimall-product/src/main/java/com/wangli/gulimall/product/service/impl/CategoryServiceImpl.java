package com.wangli.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangli.common.utils.PageUtils;
import com.wangli.common.utils.Query;
import com.wangli.gulimall.product.dao.CategoryDao;
import com.wangli.gulimall.product.entity.CategoryEntity;
import com.wangli.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查找所有商品目录分类，父子结构
     *
     * @return
     */
    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        return categoryEntities.stream().filter(entity -> entity.getParentCid() == 0)
                .peek(e -> e.setChildren(this.getChildrens(e, categoryEntities)))
                .sorted(Comparator.comparingInt(o -> (o.getSort() == null ? 0 : o.getSort())))
                .collect(Collectors.toList());
    }

    /**
     * 设置当前类名的子类名，递归设置
     *
     * @param current
     * @param all
     * @return
     */
    private List<CategoryEntity> getChildrens(CategoryEntity current, List<CategoryEntity> all) {
        return all.stream().filter(e -> current.getCatId().equals(e.getParentCid()))
                .peek(e -> e.setChildren(getChildrens(e, all)))
                .sorted(Comparator.comparingInt(o -> (o.getSort() == null ? 0 : o.getSort())))
                .collect(Collectors.toList());
    }

}