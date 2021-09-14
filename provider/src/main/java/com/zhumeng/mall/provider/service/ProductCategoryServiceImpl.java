package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhumeng.api.model.PmsProductCategory;
import com.zhumeng.api.service.IProductCategoryService;
import com.zhumeng.api.service.ITokenService;
import com.zhumeng.mall.provider.mapper.PmsProductCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/3/21:30
 * 描述你的类：
 * @author Lenovo
 */
@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.IProductCategoryService",
        interfaceClass = IProductCategoryService.class,
        timeout = 120000
)
public class ProductCategoryServiceImpl implements IProductCategoryService {
    @Autowired
    private PmsProductCategoryMapper pdao;
    @Cacheable(cacheNames = {"ProductCategoryList"},unless = "#result==null")
    @Override
    public List<PmsProductCategory> listWithChildren() {
        return  pdao.listWithChildren();
    }
}
