package com.zhumeng.mall.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhumeng.api.anno.UserLoginToken;
import com.zhumeng.api.common.CommonPage;
import com.zhumeng.api.common.CommonResult;
import com.zhumeng.api.model.PmsProductAttribute;
import com.zhumeng.api.service.IPmsProductService;
import com.zhumeng.api.service.IProductAttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/26/23:57
 * 描述你的类：
 * @author Lenovo
 */
@RestController
@CrossOrigin
@Api(tags = "PmsProductAttributeController", description = "商品属性管理")
@RequestMapping("/productAttribute")
public class ProductAttributeController {
    @Reference(
            version = "1.0.0",interfaceClass = IProductAttributeService.class,
            interfaceName = "com.zhumeng.api.service.IProductAttributeService",
            timeout =120000
    )
    private IProductAttributeService productAttributeService;
    @ApiOperation("根据分类查询属性列表或参数列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "0表示属性，1表示参数", required = true, paramType = "query", dataType = "integer")})
    @RequestMapping(value = "/list/{cid}", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    //   @SystemLog(description = "根据分类查询属性列表或参数列表", type = LogType.PRODUCT_ATTRIBUTE_LIST)
    public CommonResult<CommonPage<PmsProductAttribute>> getList(@PathVariable Long cid,
                                                                 @RequestParam(value = "type") Integer type,
                                                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return CommonResult.success(productAttributeService.getList(cid,type,pageSize,pageNum));
    }
}
