package com.zhumeng.api.service;

import com.zhumeng.api.common.CommonPage;

/**
 * 创建人：Jason
 * 创建时间：2020/5/26
 * 描述你的类：
 */


public interface IBrandService {
    CommonPage listBrand(String keyword, int pageNum, int pageSize);
}
