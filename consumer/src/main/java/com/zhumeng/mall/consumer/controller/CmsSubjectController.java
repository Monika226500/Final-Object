package com.zhumeng.mall.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhumeng.api.anno.UserLoginToken;
import com.zhumeng.api.common.CommonResult;
import com.zhumeng.api.model.CmsSubject;
import com.zhumeng.api.service.ICmsSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/4/23:34
 * 描述你的类：
 */
@RestController
@Api(tags = "CmsSubjectController", description = "商品专题管理")
@RequestMapping("/subject")
@CrossOrigin
public class CmsSubjectController {
    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.ICmsSubjectService",
            interfaceClass = ICmsSubjectService.class,
            timeout = 120000
    )
    private ICmsSubjectService service;

    @ApiOperation("获取全部商品专题")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    public CommonResult<List<CmsSubject>> listAll() {

        return CommonResult.success(service.listAll());

    }
}
