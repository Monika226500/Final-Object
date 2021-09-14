package com.zhumeng.mall.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhumeng.api.anno.UserLoginToken;
import com.zhumeng.api.common.CommonPage;
import com.zhumeng.api.common.CommonResult;
import com.zhumeng.api.model.PmsBrand;
import com.zhumeng.api.service.IBrandService;
import com.zhumeng.api.service.IProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/4/22:52
 * 描述你的类：
 */
@RestController
@CrossOrigin
@Api(tags = "BrandController", description = "品牌管理")
@RequestMapping("/brand")


public class BrandController {
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.IBrandService",
            interfaceClass = IBrandService.class,
            timeout = 120000
    )
    private IBrandService brandService;
    @ApiOperation(value = "根据品牌名称分页获取品牌列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    //@SystemLog(description = "品牌列表", type = LogType.BRAND_LIST)
    @ResponseBody
    @UserLoginToken
    public CommonResult<CommonPage<PmsBrand>> getList(@RequestParam(value = "keyword", required = false) String keyword,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        CommonPage<PmsBrand> cp=brandService.listBrand(keyword,pageNum,pageSize);
        return CommonResult.success(cp);
    }
}

