package com.zhumeng.mall.provider.component;

import com.zhumeng.api.service.IOmsOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/10/21:37
 * 描述你的类：
 */
@Component
@RabbitListener(queues = {"mall.order.cancel"})
public class CancelOrderReceiver {
    private static Logger LOGGER = LoggerFactory.getLogger(CancelOrderReceiver.class);
    @Autowired
    private IOmsOrderService service;

    @RabbitHandler
    public void handler(Long orderId){
        service.cancelOrder(orderId);
        LOGGER.info("process orderId:{}",orderId);
    }

    }
