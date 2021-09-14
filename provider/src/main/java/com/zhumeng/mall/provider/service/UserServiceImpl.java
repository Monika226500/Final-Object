package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.zhumeng.api.model.UmsAdmin;
import com.zhumeng.api.service.ITokenService;
import com.zhumeng.api.service.IUserService;
import com.zhumeng.mall.provider.mapper.UmsAdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人：朱蒙
 * 创建时间：2021/1/26/20:06
 * 描述你的类：
 * @author Lenovo
 */

@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.IUserService",
        interfaceClass = IUserService.class,
        timeout = 120000
)
@Transactional
public class UserServiceImpl implements IUserService {
    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.ITokenService",
            interfaceClass = ITokenService.class,
            timeout = 120000
    )
    private ITokenService tokenService;


    @Autowired
    private UmsAdminMapper udao;

    @Override
    public UmsAdmin findUserById(Long userId) {
        UmsAdmin admin=udao.selectByPrimaryKey(userId);
        //通过主键找到用户
        return admin;
        //输出admin
    }

    /**
     * 通过用户名找到当前对象
     * @param username
     * @return
     */
    @Override
    public UmsAdmin findByUsername(String username) {
        return udao.selectByUsername(username);
    }

    @Override
    public UmsAdmin reg(UmsAdmin user) {
        user.setIcon("http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20190129/170157_yIl3_1767531.jpg");
        udao.insert(user);
        return user;
    }

    @Override
    public UmsAdmin findByUmsAdmin(String token) {
        String userid=tokenService.getUserId(token);
        return this.findUserById(Long.valueOf(userid));
    }
}
