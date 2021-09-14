package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhumeng.api.common.CommonPage;
import com.zhumeng.api.model.PmsProductAttributeCategory;
import com.zhumeng.api.model.PmsProductAttributeCategoryExample;
import com.zhumeng.api.service.IPmsProductAttributeCategoryService;
import com.zhumeng.mall.provider.mapper.PmsProductAttributeCategoryMapper;
import com.zhumeng.mall.provider.mapper.PmsProductCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/21/21:10
 * 描述你的类：
 */
@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.IPmsProductAttributeCategoryService",
        interfaceClass = IPmsProductAttributeCategoryService.class,
        timeout = 120000
)
public class PmsProductAttributeCategoryServiceImpl implements IPmsProductAttributeCategoryService {
    @Autowired
    private PmsProductAttributeCategoryMapper pdao;

    @Cacheable(cacheNames = {"ProductAttributeCategoryList"},
            unless = "#result==null",key = "'acl'+#pageNum+'-'+#pageSize")
    //当结果为空时不缓存

    @Override
    public CommonPage getList(Integer pageSize, Integer pageNum) {
        List<PmsProductAttributeCategory> list=pdao.selectByExample(new PmsProductAttributeCategoryExample());

        return CommonPage.restPage(list);
    }
}
