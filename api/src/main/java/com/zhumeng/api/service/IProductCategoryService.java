package com.zhumeng.api.service;

import com.zhumeng.api.model.PmsProductCategory;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/3/21:26
 * 描述你的类：
 */
public interface IProductCategoryService {
    List<PmsProductCategory> listWithChildren();
}
