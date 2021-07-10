package com.wangli.gulimall.search.service;

import com.wangli.gulimall.search.vo.SearchParam;
import com.wangli.gulimall.search.vo.SearchResult;

/**
 * @author WangLi
 * @date 2021/7/10 13:56
 * @description
 */
public interface MallSearchService {
    SearchResult search(SearchParam searchParam);
}
