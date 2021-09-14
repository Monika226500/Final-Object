package com.zhumeng.api.service;

import com.zhumeng.api.model.CmsSubject;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/4/23:17
 * 描述你的类：
 */
public interface ICmsSubjectService {
    /*
    查询所有专题
     */
    List<CmsSubject> listAll();
}
