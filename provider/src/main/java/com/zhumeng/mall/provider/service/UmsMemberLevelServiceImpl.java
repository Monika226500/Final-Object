package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhumeng.api.model.UmsMemberLevel;
import com.zhumeng.api.model.UmsMemberLevelExample;
import com.zhumeng.api.service.IUMSLogService;
import com.zhumeng.api.service.IUmsMemberLevelService;
import com.zhumeng.mall.provider.mapper.UmsMemberLevelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/5/11:32
 * 描述你的类：
 */
@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.IUmsMemberLevelService",
        interfaceClass = IUmsMemberLevelService.class,
        timeout = 120000
)
public class UmsMemberLevelServiceImpl implements IUmsMemberLevelService {
    @Autowired
    private UmsMemberLevelMapper umsMemberLevelMapper;
    @Cacheable(cacheNames = {"UmsMemberLevelList"},unless = "#result==null")//1:05:03

    @Override
    public List<UmsMemberLevel> list(Integer defaultStatus) {
        UmsMemberLevelExample example=new UmsMemberLevelExample();
        example.createCriteria().andDefaultStatusEqualTo(0);
        return umsMemberLevelMapper.selectByExample(new UmsMemberLevelExample());
    }
}
