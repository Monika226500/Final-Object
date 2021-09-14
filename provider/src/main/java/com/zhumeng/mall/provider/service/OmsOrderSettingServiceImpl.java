package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhumeng.api.model.OmsOrderSetting;
import com.zhumeng.api.service.IOmsOrderService;
import com.zhumeng.api.service.IOmsOrderSettingService;
import com.zhumeng.mall.provider.mapper.OmsOrderSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/10/20:08
 * 描述你的类：
 */
@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.IOmsOrderSettingService",
        interfaceClass = IOmsOrderSettingService.class,
        timeout = 120000
)
public class OmsOrderSettingServiceImpl implements IOmsOrderSettingService {

    @Autowired
    private OmsOrderSettingMapper omsOrderSettingMapper;
    @Override
    public OmsOrderSetting getItem(Long id) {
        return omsOrderSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public OmsOrderSetting update(Long id, OmsOrderSetting orderSetting) {
        orderSetting.setId(id);
        omsOrderSettingMapper.updateByPrimaryKey(orderSetting);
        return orderSetting;
    }
}
