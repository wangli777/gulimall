package com.wangli.gulimall.search.service;

import com.wangli.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author WangLi
 * @date 2021/6/22 20:38
 * @description
 */

public interface ProductSaveService {

    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
