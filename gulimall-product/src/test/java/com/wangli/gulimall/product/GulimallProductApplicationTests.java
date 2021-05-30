package com.wangli.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangli.gulimall.product.entity.BrandEntity;
import com.wangli.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;
    @Test
    void save() {
        BrandEntity entity = new BrandEntity();
        entity.setName("小米");
        brandService.save(entity);
        System.out.println("保存成功");
    }

    @Test
    void update() {
        BrandEntity entity = new BrandEntity();
        entity.setBrandId(1L);
        entity.setDescript("hello");
        brandService.updateById(entity);
        System.out.println("更新成功");
    }

    @Test
    void search() {
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach(System.out::println);
    }

}
