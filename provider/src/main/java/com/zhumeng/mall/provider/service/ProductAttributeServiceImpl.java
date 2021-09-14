package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.zhumeng.api.common.CommonPage;
import com.zhumeng.api.model.PmsProductAttribute;
import com.zhumeng.api.model.PmsProductAttributeExample;
import com.zhumeng.api.service.IProductAttributeService;
import com.zhumeng.api.service.IProductCategoryService;
import com.zhumeng.mall.provider.mapper.PmsProductAttributeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/27/21:53
 * 描述你的类：
 */
@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.IProductAttributeService",
        interfaceClass = IProductAttributeService.class,
        timeout = 120000
)
public class ProductAttributeServiceImpl implements IProductAttributeService {
    @Autowired
    private PmsProductAttributeMapper productAttributeMapper;
    @Cacheable(cacheNames= {"ProductAttributeList"},
            unless="#result == null",key = "#cid+'-'+#type+'-'+#pageNum+'-'+#pageSize")
    @Override
    public CommonPage getList(Long cid, Integer type, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        PmsProductAttributeExample example=new PmsProductAttributeExample();
        example.setOrderByClause("sort desc");
        example.createCriteria().andProductAttributeCategoryIdEqualTo(cid).andTypeEqualTo(type);
        List<PmsProductAttribute> list=productAttributeMapper.selectByExample(example);
        return CommonPage.restPage(list);
    }
}
