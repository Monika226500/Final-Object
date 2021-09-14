package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.zhumeng.api.common.CommonPage;
import com.zhumeng.api.dto.OmsOrderQueryParam;
import com.zhumeng.api.model.*;
import com.zhumeng.api.service.IOmsOrderService;
import com.zhumeng.api.service.IPmsProductAttributeCategoryService;
import com.zhumeng.mall.provider.component.CancelOrderSender;
import com.zhumeng.mall.provider.mapper.OmsOrderItemMapper;
import com.zhumeng.mall.provider.mapper.OmsOrderMapper;
import com.zhumeng.mall.provider.mapper.PmsSkuStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/9/12:21
 * 描述你的类：
 */

@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.IOmsOrderService",
        interfaceClass = IOmsOrderService.class,
        timeout = 120000
)
public class OmsOrderServiceImpl implements IOmsOrderService {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private OmsOrderMapper odao;
    @Autowired
    private OmsOrderItemMapper omsOrderItemMapper;
    @Autowired
    private PmsSkuStockMapper pmsSkuStockMapper;
    @Autowired
    private CancelOrderSender cancelOrderSender;
    @Cacheable(cacheNames = {"OrderList"},unless = "#result==null",
            key = "T(String).valueOf(#pageNum+'-'+#pageSize)"+
                    ".concat(#queryParam.orderSn!=null?#queryParam.orderSn:'os')"+
                    ".concat(#queryParam.receiverKeyword!=null?#queryParam.receiverKeyword:'r') "+
                    ".concat(#queryParam.status!=null?#queryParam.status:'s') "+
                    ".concat(#queryParam.orderType!=null ?#queryParam.orderType:'ot') "+
                    ".concat(#queryParam.sourceType!=null ?#queryParam.sourceType:'st')"+
                    ".concat(#queryParam.createTime!=null?#queryParam.createTime:'ct')"
    )/**
     * 当结果为空是不缓存
     */

    @Override
    public CommonPage list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<OmsOrder> list=odao.getOrderList(queryParam);

        return CommonPage.restPage(list);
        //分页的形式发送到Controller

    }
    @Cacheable(cacheNames = {"Order"},unless = "#result==null",key = "#id")
    @Override
    public OmsOrder detail(Long id) {
        return odao.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        clearOrder();
        OmsOrder omsOrder=new OmsOrder();
        omsOrder.setDeleteStatus(1);
        //设置删除标记
        OmsOrderExample example=new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdEqualTo(id);

        return odao.updateByExampleSelective(omsOrder,example);
    }

    @Override
    public void sendDelayMessageCancelOrder(Long orderId, int minute) {
        long delayTimes=minute*60*1000;
        cancelOrderSender.sendMessage(orderId, delayTimes);
    }

    @CachePut(cacheNames = {"Order"},key = "#orderId")
    @Override
    public OmsOrder cancelOrder(Long orderId) {
        //取消该订单
        clearOrder();
        OmsOrder omsOrder=this.detail(orderId);

        if(omsOrder!=null){
            omsOrder.setStatus(4);
            //取消状态
            odao.updateByPrimaryKey(omsOrder);
            //取出在订单中的顶点项目，之后接触库存锁
            OmsOrderItemExample example=new OmsOrderItemExample();
            example.createCriteria().andOrderIdEqualTo(orderId);
            List<OmsOrderItem> list=omsOrderItemMapper.selectByExample(example);
            //用lamada进行map，进去出产品id组合成新的list
            List<Long> productidList=list.stream().map(c->c.getProductId()).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(productidList)){
                //解除该产品编号指定的库存
                PmsSkuStockExample example1=new PmsSkuStockExample();
                example1.createCriteria().andProductIdIn(productidList);
                pmsSkuStockMapper.selectByExample(example1);
                PmsSkuStock skuStock=new PmsSkuStock();
                skuStock.setLockStock(0);
                pmsSkuStockMapper.updateByExampleSelective(skuStock,example1);

            }

        }
        return omsOrder;
    }

    private void clearOrder() {
        cacheManager.getCache("OrderList").clear();
    }
}
