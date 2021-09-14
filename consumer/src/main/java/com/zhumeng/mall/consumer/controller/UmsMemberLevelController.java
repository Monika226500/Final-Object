package com.zhumeng.mall.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhumeng.api.anno.UserLoginToken;
import com.zhumeng.api.common.CommonResult;
import com.zhumeng.api.model.UmsMemberLevel;
import com.zhumeng.api.service.IUmsMemberLevelService;
import com.zhumeng.api.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/5/11:41
 * 描述你的类：
 */
@Controller
@Api(tags = "UmsMemberLevelController", description = "会员等级管理")
@RequestMapping("/memberLevel")
@CrossOrigin
public class UmsMemberLevelController {
    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.IUmsMemberLevelService",
            interfaceClass = IUmsMemberLevelService.class,
            timeout = 120000
    )
    private IUmsMemberLevelService memberLevelService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("查询所有会员等级")
    @ResponseBody
    @UserLoginToken
    //@SystemLog(description = "查询所有会员等级", type = LogType.UMS_MEMBER_LEVEL_LIST)
    public CommonResult<List<UmsMemberLevel>> list(@RequestParam("defaultStatus") Integer defaultStatus) {
        List<UmsMemberLevel> memberLevelList = memberLevelService.list(defaultStatus);
        return CommonResult.success(memberLevelList);
    }
}
