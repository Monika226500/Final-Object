package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.zhumeng.api.common.CommonPage;
import com.zhumeng.api.model.PmsBrand;
import com.zhumeng.api.model.PmsBrandExample;
import com.zhumeng.api.service.IBrandService;
import com.zhumeng.api.service.IUserService;
import com.zhumeng.mall.provider.mapper.PmsBrandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/4/21:55
 * 描述你的类：
 */
@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.IBrandService",
        interfaceClass = IBrandService.class,
        timeout = 120000
)
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private PmsBrandMapper bdao;
    @Cacheable(cacheNames = {"ProductBrandList"},unless = "#result==null")
    @Override
    public CommonPage listBrand(String keyword, int pageNum, int pageSize) {
        //配置好PageHelper
        PageHelper.startPage(pageNum,pageSize);
        PmsBrandExample example=new PmsBrandExample();
        example.setOrderByClause("sort desc");
        List<PmsBrand> list=bdao.selectByExample(example);
        return CommonPage.restPage(list);
    }
}
