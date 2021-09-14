package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhumeng.api.model.CmsPrefrenceArea;
import com.zhumeng.api.model.CmsPrefrenceAreaExample;
import com.zhumeng.api.service.ICmsPrefrenceAreaService;
import com.zhumeng.mall.provider.mapper.CmsPrefrenceAreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/4/22:01
 * 描述你的类：
 * @author mine
 */
@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.ICmsPreferenceAreaService",
        interfaceClass = ICmsPrefrenceAreaService.class,
        timeout = 120000
)
public class CmsPrefrenceAreaServiceImpl implements ICmsPrefrenceAreaService {
    @Autowired
    private CmsPrefrenceAreaMapper cmsPrefrenceAreaMapper;
    @Cacheable(cacheNames = {"CmsPreferenceAreaList"},unless = "#result==null")
    @Override
    public List<CmsPrefrenceArea> listAll() {
        return cmsPrefrenceAreaMapper.selectByExample(new CmsPrefrenceAreaExample());
    }
}
