package com.yaoan.module.system.mq.basic.rabbitmq.config;


import com.yaoan.module.system.mq.basic.MqConst;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author doujiale
 * @description Queue队列注入
 */
@Configuration
public class BuyManQueueConfiguration {

    /**
     * 采购人单位信息注册同步队列
     *
     * @return Queue
     */
    @Bean(name = "gpxBasicGpbpBuyManRegisterQueue")
    public Queue getGpxBasicGpbpBuyManRegisterQueue() {
        return new Queue(MqConst.RabbitMq.BuyManQueue.REGISTER.getCode());
    }

    /**
     * 采购人单位删除
     *
     * @return Queue
     */
    @Bean(name = "gpxBasicGpbpBuyManRemoveQueue")
    public Queue getGpxBasicGpbpBuyManRemoveQueue() {
        return new Queue(MqConst.RabbitMq.BuyManQueue.REMOVE.getCode());
    }

    /**
     * 采购人单位 修改队列
     *
     * @return Queue
     */
    @Bean(name = "gpxBasicGpbpBuyManUpdateQueue")
    public Queue getGpxBasicGpbpBuyManUpdateQueue() {
        return new Queue(MqConst.RabbitMq.BuyManQueue.UPDATE.getCode());
    }

    /**
     * 采购人单位人员信息注册同步队列
     *
     * @return Queue
     */
    @Bean(name = "gpxBasicGpbpBuyManUserQueue")
    public Queue getGpxBasicGpbpBuyManUserQueue() {
        return new Queue(MqConst.RabbitMq.BuyManQueue.USER_REGISTER.getCode());
    }

    /**
     * 采购人人员信息变更
     *
     * @return Queue
     */
    @Bean(name = "gpxBasicGpbpBuyManUpdateUserQueue")
    public Queue getGpxBasicGpbpBuyManUpdateUserQueue() {
        return new Queue(MqConst.RabbitMq.BuyManQueue.USER_UPDATE.getCode());
    }


}
