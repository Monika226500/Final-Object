package com.zhumeng.api.service;

import com.zhumeng.api.common.CommonPage;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/27/21:51
 * 描述你的类：
 */
public interface IProductAttributeService {
    /**
     * 根据分类分页获取商品属性
     * @param cid 分类id
     * @param type 0->属性；2->参数
     * @return
     */
    CommonPage getList(Long cid, Integer type, Integer pageSize, Integer pageNum);
}
