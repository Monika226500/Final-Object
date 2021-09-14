package com.zhumeng.mall.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhumeng.api.anno.LogType;
import com.zhumeng.api.anno.SystemLog;
import com.zhumeng.api.anno.UserLoginToken;
import com.zhumeng.api.common.CommonPage;
import com.zhumeng.api.common.CommonResult;
import com.zhumeng.api.dto.OmsOrderQueryParam;
import com.zhumeng.api.model.OmsOrder;
import com.zhumeng.api.service.IOmsOrderService;
import com.zhumeng.api.service.IProductAttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/9/11:45
 * 描述你的类：
 */
@Controller
@Api(tags = "OmsOrderController", description = "订单管理")
@RequestMapping("/order")
@CrossOrigin
public class OmsOrderController {
    @Reference(
            version = "1.0.0",interfaceClass = IOmsOrderService.class,
            interfaceName = "com.zhumeng.api.service.IOmsOrderService",
            timeout =120000
    )
    private IOmsOrderService orderService;
    @ApiOperation("查询订单")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "查询订单", type = LogType.ORDER_LIST)
    public CommonResult<CommonPage<OmsOrder>> list(OmsOrderQueryParam queryParam,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {

        return CommonResult.success(orderService.list(queryParam, pageSize, pageNum));
    }

    @ApiOperation("获取订单详情:订单信息、商品信息、操作记录")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "获取订单详情:订单信息、商品信息、操作记录", type = LogType.ORDER_DETAIL)
    public CommonResult<OmsOrder> detail(@PathVariable Long id) {
        OmsOrder orderDetailResult = orderService.detail(id);
        return CommonResult.success(orderDetailResult);
    }

    @ApiOperation("批量删除订单")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "批量删除订单", type = LogType.ORDER_DELETE)
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {

        CommonResult commonResult;
        try {
            ids.forEach(c -> orderService.delete(c));
            commonResult = CommonResult.success(1);
        }catch(Exception e){
            commonResult = CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;
    }
    @ApiOperation("取消单个超时订单")
    @RequestMapping(value = "/cancelOrder",method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "取消单个超时订单", type = LogType.ORDER_CANCEL)
    public CommonResult cancelOrder(@RequestParam("ids") List<Long> ids,@RequestParam("minute") int minute){
        orderService.sendDelayMessageCancelOrder(ids.get(0),minute);
        return CommonResult.success(null);
    }
}
