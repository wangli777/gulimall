package com.wangli.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangli.common.utils.PageUtils;
import com.wangli.gulimall.product.entity.SkuInfoEntity;
import com.wangli.gulimall.product.vo.web.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author wangli
 * @email 1151723225@qq.com
 * @date 2021-05-30 12:31:04
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);

    List<SkuInfoEntity> getSkusBySpuId(Long spuId);

    SkuItemVo item(Long skuId);
}

