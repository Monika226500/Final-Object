package com.zhumeng.api.service;

import com.zhumeng.api.common.CommonPage;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/21/21:16
 * 描述你的类：
 */
public interface IPmsProductAttributeCategoryService {
    CommonPage getList(Integer pageSize, Integer pageNum);
}
