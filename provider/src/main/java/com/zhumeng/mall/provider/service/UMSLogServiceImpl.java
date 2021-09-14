package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhumeng.api.model.UMSLog;
import com.zhumeng.api.service.ITokenService;
import com.zhumeng.api.service.IUMSLogService;
import com.zhumeng.mall.provider.mapper.UMSLogMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/17/11:19
 * 描述你的类：
 */

@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.IUMSLogService",
        interfaceClass = IUMSLogService.class,
        timeout = 120000
)
public class UMSLogServiceImpl implements IUMSLogService {
    @Autowired
    private UMSLogMapper dao;
    @Override
    public Integer insert(UMSLog log) {
        return dao.insert(log);
    }
}
