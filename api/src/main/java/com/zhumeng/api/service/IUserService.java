package com.zhumeng.api.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhumeng.api.model.UmsAdmin;

/**
 * 创建人：朱蒙
 * 创建时间：2021/1/26/20:04
 * 描述你的类：
 * @author Lenovo
 */

@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.IUserService",
        interfaceClass = IUserService.class,
        timeout = 120000
)

public interface IUserService {
    //    public UmsAdmin login(UmsAdminLoginParam user) ;
    public UmsAdmin findUserById(Long userId);
    public UmsAdmin findByUsername(String username);
    public UmsAdmin reg(UmsAdmin user) ;
    public UmsAdmin findByUmsAdmin(String token);
}
