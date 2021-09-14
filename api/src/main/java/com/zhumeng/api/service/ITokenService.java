package com.zhumeng.api.service;

/**
 * 创建人：朱蒙
 * 创建时间：2021/1/26/15:23
 * 描述你的类：
 * @author Lenovo
 */
public interface ITokenService {
    public  String getToken(String userId,String password);//需要写token的生成方法
    public  String getUserId(String token);//通过token得到用户id
    public  boolean checkSign(String token,String password);//通过token验证密码
}
