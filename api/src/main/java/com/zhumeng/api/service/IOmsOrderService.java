package com.zhumeng.api.service;

import com.zhumeng.api.common.CommonPage;
import com.zhumeng.api.dto.OmsOrderQueryParam;
import com.zhumeng.api.model.OmsOrder;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/9/11:51
 * 描述你的类：
 */
public interface IOmsOrderService {
    public CommonPage list(OmsOrderQueryParam queryParam,Integer pageSize,Integer pageNum);
    public OmsOrder detail(Long id);
    public int delete(Long id);
    public void sendDelayMessageCancelOrder(Long orderId,int minute);
    public OmsOrder cancelOrder(Long orderId);
}
