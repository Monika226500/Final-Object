package com.zhumeng.mall.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhumeng.api.anno.UserLoginToken;
import com.zhumeng.api.common.CommonResult;
import com.zhumeng.api.model.CmsPrefrenceArea;
import com.zhumeng.api.service.ICmsPrefrenceAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/28/23:27
 * 描述你的类：
 */
@RestController
@Api(tags = "CmsPrefrenceAreaController",description = "商品优选管理")
@RequestMapping("/prefrenceArea")
@CrossOrigin
public class CmsPrefrenceAreaController {
    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.ICmsPrefrenceAreaService",
            interfaceClass = ICmsPrefrenceAreaService.class,
            timeout = 120000
    )
    private ICmsPrefrenceAreaService service;
    @ApiOperation("获取所有商品优选")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    public CommonResult<List<CmsPrefrenceArea>> listAll(){
        return CommonResult.success(service.listAll());
    }
}
