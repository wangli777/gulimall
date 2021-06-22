package com.wangli.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangli.common.utils.PageUtils;
import com.wangli.gulimall.ware.entity.WareSkuEntity;
import com.wangli.gulimall.ware.vo.SkuHasStockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author wangli
 * @email 1151723225@qq.com
 * @date 2021-05-30 13:56:35
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);
}

