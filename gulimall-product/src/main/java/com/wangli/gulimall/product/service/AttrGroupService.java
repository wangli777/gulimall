package com.wangli.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangli.common.utils.PageUtils;
import com.wangli.gulimall.product.entity.AttrGroupEntity;

import java.util.Map;

/**
 * 属性分组
 *
 * @author wangli
 * @email 1151723225@qq.com
 * @date 2021-05-30 12:31:04
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);
}

