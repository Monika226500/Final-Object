package com.zhumeng.mall.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhumeng.api.anno.LogType;
import com.zhumeng.api.anno.SystemLog;
import com.zhumeng.api.anno.UserLoginToken;
import com.zhumeng.api.common.CommonResult;
import com.zhumeng.api.model.OmsOrderSetting;
import com.zhumeng.api.service.IOmsOrderService;
import com.zhumeng.api.service.IOmsOrderSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/10/20:34
 * 描述你的类：
 */
@RestController
@Api(tags = "OmsOrderSettingController", description = "订单设置管理")
@RequestMapping("/orderSetting")
@CrossOrigin
public class OmsOrderSettingController {
    @Reference(
            version = "1.0.0",interfaceClass = IOmsOrderSettingService.class,
            interfaceName = "com.zhumeng.api.service.IOmsOrderSettingService",
            timeout =120000
    )
    private IOmsOrderSettingService omsOrderSettingService;
    @ApiOperation("获取指定订单设置")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "获取指定订单设置", type = LogType.ORDER_DETAIL_SETTING)
    public CommonResult<OmsOrderSetting> getItem(@PathVariable Long id) {

        return CommonResult.success(omsOrderSettingService.getItem(id));
    }

    @ApiOperation("修改指定订单设置")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "获取指定订单设置", type = LogType.ORDER_UPDATE_DETAIL_SETTING)
    public CommonResult update(@PathVariable Long id, @RequestBody OmsOrderSetting orderSetting) {
        CommonResult commonResult;
        try{
            omsOrderSettingService.update(id,orderSetting);
            commonResult = CommonResult.success(1);//修改成功一条记录
        }catch(Exception e){
            commonResult = CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;

    }

}
