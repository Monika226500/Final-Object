package com.zhumeng.mall.provider.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 创建人：朱蒙
 * 创建时间：2021/3/10/21:31
 * 描述你的类：
 * @author Lenovo
 */
@Component
public class CancelOrderSender {
    private static Logger LOGGER = LoggerFactory.getLogger(CancelOrderSender.class);
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(Long orderId,final long delayTimes){
        amqpTemplate.convertAndSend(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(),
                QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(), orderId, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        //给消息设置延迟毫秒值
                        message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
                        return message;
                    }
                });
        LOGGER.info("send orderId:{}",orderId);
    }
}
