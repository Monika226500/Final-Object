package com.zhumeng.mall.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhumeng.api.anno.UserLoginToken;
import com.zhumeng.api.common.CommonResult;
import com.zhumeng.api.model.PmsProductCategory;
import com.zhumeng.api.service.IProductCategoryService;
import com.zhumeng.api.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/3/21:22
 * 描述你的类：
 * @author Lenovo
 */
@RestController
@CrossOrigin
@Api(tags = "PmsProductCategoryController", description = "商品分类管理")
@RequestMapping("/productCategory")
public class PmsProductCategoryController {
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.IProductCategoryService",
            interfaceClass = IProductCategoryService.class,
            timeout = 120000
    )
    private IProductCategoryService productCategoryService;

    //添加商品菜单用
    @ApiOperation("查询所有一级分类及子分类")
    @RequestMapping(value = "/list/withChildren", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    //@SystemLog(description = "查询所有一级分类及子分类", type = LogType.PRODUCT_WITH_CHILDREN)
    public CommonResult<List<PmsProductCategory>> listWithChildren() {
        return CommonResult.success(productCategoryService.listWithChildren());
    }
}
