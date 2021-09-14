package com.zhumeng.mall.consumer.aop;

import com.zhumeng.api.model.UMSLog;
import com.zhumeng.api.service.IUMSLogService;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/17/11:28
 * 描述你的类：
 */
public class SaveSystemLogThread implements Runnable{
    private IUMSLogService service;
    private UMSLog log;
    public SaveSystemLogThread(IUMSLogService service, UMSLog log){
        this.service=service;
        this.log=log;
    }
    @Override
    public void run() {
        this.service.insert(this.log);
    }
}
