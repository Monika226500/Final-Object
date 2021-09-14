package com.zhumeng.mall.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhumeng.api.anno.UserLoginToken;
import com.zhumeng.api.common.CommonPage;
import com.zhumeng.api.common.CommonResult;
import com.zhumeng.api.model.PmsProductAttributeCategory;
import com.zhumeng.api.service.IPmsProductAttributeCategoryService;
import com.zhumeng.api.service.IPmsProductService;
import com.zhumeng.api.service.IProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/19/22:15
 * 描述你的类：
 */
@RestController
@CrossOrigin
@Api(tags = "PmsProductAttributeCategoryController", description = "商品属性分类管理")
@RequestMapping("/productAttribute/category")
public class PmsProductAttributeCategoryController {
    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.IPmsProductAttributeCategoryService",
            interfaceClass = IPmsProductAttributeCategoryService.class,
            timeout = 120000
    )
    private IPmsProductAttributeCategoryService productAttributeCategoryService;

    @ApiOperation("分页获取所有商品属性分类")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    public CommonResult<CommonPage<PmsProductAttributeCategory>> getList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "100") Integer pageSize
    ){
        return CommonResult.success(productAttributeCategoryService.getList(pageSize,pageNum));
    }
}
