package com.zhumeng.api.service;

import com.zhumeng.api.model.OmsOrderSetting;

/**
 * 订单设置Service

 */
public interface IOmsOrderSettingService {
    /**
     * 获取指定订单设置
     */
    OmsOrderSetting getItem(Long id);

    /**
     * 修改指定订单设置
     */
    OmsOrderSetting update(Long id, OmsOrderSetting orderSetting);
}
