package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.zhumeng.api.service.ITokenService;

import java.util.Date;

/**
 * 创建人：朱蒙
 * 创建时间：2021/1/26/15:29
 * 描述你的类：
 * @author Lenovo
 */

@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.ITokenService",
        interfaceClass = ITokenService.class,
        timeout = 120000
)
public class TokenServiceImpl implements ITokenService {
    /**
     * 过期时间为五分钟
     */
    private static final long EXPIRE_TIME=5 * 600 * 10000;
    @Override
    public String getToken(String userId, String password) {
        String token="";
        //声明token的过期时间，过期就失效了
        Date date=new Date(System.currentTimeMillis()+EXPIRE_TIME);

        token=JWT.create().withAudience(userId).withExpiresAt(date).sign(Algorithm.HMAC256(password));
        return token;
    }

    @Override
    public String getUserId(String token) {
        String userid= JWT.decode(token).getAudience().get(0);
        //JWT可存储区
        return userid;
    }

    /**
     *
     * @param token token令牌
     * @param password 密码
     * @return 是否验证成功
     */
    @Override
    public boolean checkSign(String token, String password) {

        if(token==null){
            throw new RuntimeException("无token，需要重新登陆");
        }
        try {
            //通过JWT先进行256加密再进行校验
            JWTVerifier verifier=JWT.require(Algorithm.HMAC256(password)).build();

            //校验器专门对token校验
            verifier.verify(token);
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }
}
