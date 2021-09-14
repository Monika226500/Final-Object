package com.zhumeng.api.service;

import com.zhumeng.api.model.UmsMemberLevel;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/5/11:29
 * 描述你的类：
 */
public interface IUmsMemberLevelService {
    /*
    获取所有会员登录
    @param defaultStatus 是否成为默认会员
     */
    List<UmsMemberLevel> list(Integer defaultStatus);
}
