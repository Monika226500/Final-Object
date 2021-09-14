package com.zhumeng.mall.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhumeng.api.anno.LogType;
import com.zhumeng.api.anno.SystemLog;
import com.zhumeng.api.anno.UserLoginToken;
import com.zhumeng.api.common.CommonPage;
import com.zhumeng.api.common.CommonResult;
import com.zhumeng.api.dto.PmsProductQueryParam;
import com.zhumeng.api.model.PmsProduct;
import com.zhumeng.api.service.IPmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/5/16:20
 * 描述你的类：
 * @author Lenovo
 */
@RestController
@CrossOrigin
@Api(tags = "PmsProductController", description = "产品管理")
@RequestMapping("/product")
public class PmsProductController {
    @Reference(
            version = "1.0.0",interfaceClass = IPmsProductService.class,
            interfaceName = "com.zhumeng.api.service.IPmsProductService",
            timeout =120000
    )
    private IPmsProductService productService;
    @ApiOperation("查询商品")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "查询商品", type = LogType.PRODUCT_LIST)
    public CommonResult<CommonPage<PmsProduct>> getList(PmsProductQueryParam productQueryParam,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {

        return CommonResult.success(productService.list(productQueryParam,pageSize,pageNum));
    }

    @ApiOperation("批量修改删除状态")
    @RequestMapping(value = "/update/deleteStatus", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "批量修改删除状态", type = LogType.PRODUCT_DELETE_STATUS)
    public CommonResult updateDeleteStatus(@RequestParam("ids") List<Long> ids,
                                           @RequestParam("deleteStatus") Integer deleteStatus) {
        int count = productService.updateDeleteStatus(ids, deleteStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量上下架")
    @RequestMapping(value = "/update/publishStatus", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "批量上下架", type = LogType.PRODUCT_PUBLISH_STATUS)
    public CommonResult updatePublishStatus(@RequestParam("ids") List<Long> ids,
                                            @RequestParam("publishStatus") Integer publishStatus) {
        CommonResult commonResult;
        try {
            ids.forEach(c->productService.updatePublishStatus(c,publishStatus));
            commonResult=CommonResult.success(1);
        }catch (Exception e){
            commonResult=CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;
    }
    @ApiOperation("创建商品")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "创建商品", type = LogType.PRODUCT_ADD)
    public CommonResult create(@RequestBody PmsProduct product) {
        CommonResult commonResult;
        try{
            productService.create(product);
            commonResult = CommonResult.success(1);//修改成功一条记录
        }catch(Exception e){
            commonResult = CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;

    }
    @ApiOperation("根据商品id获取商品编辑信息")
    @RequestMapping(value = "/updateInfo/{id}", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "根据商品id获取商品编辑信息", type = LogType.PRODUCT_ID)
    public CommonResult<PmsProduct> getUpdateInfo(@PathVariable Long id) {
        PmsProduct pp=productService.getUpdateInfo(id);
        return CommonResult.success(pp);
    }

    @ApiOperation("更新商品")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "更新商品", type = LogType.PRODUCT_UPDATE)
    public CommonResult update(@PathVariable Long id,
                               @RequestBody PmsProduct product
    ) {
        CommonResult commonResult;
        try{
            PmsProduct pp=productService.update(id,product);
            commonResult = CommonResult.success(1);//修改成功一条记录
        }catch(Exception e){
            commonResult = CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;

    }
}
