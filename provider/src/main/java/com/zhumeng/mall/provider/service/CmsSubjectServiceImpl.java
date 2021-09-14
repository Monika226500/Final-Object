package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhumeng.api.model.CmsSubject;
import com.zhumeng.api.model.CmsSubjectExample;
import com.zhumeng.api.service.ICmsSubjectService;
import com.zhumeng.mall.provider.mapper.CmsSubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/4/23:23
 * 描述你的类：
 */
@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.ICmsSubjectService",
        interfaceClass = ICmsSubjectService.class,
        timeout = 120000
)
public class CmsSubjectServiceImpl implements ICmsSubjectService {
    @Autowired
    private CmsSubjectMapper cmsSubjectMapper;
    @Cacheable(cacheNames = {"CmsSubjectList"},unless = "#result==null")
    @Override
    public List<CmsSubject> listAll() {
        return cmsSubjectMapper.selectByExample(new CmsSubjectExample());
    }
}
