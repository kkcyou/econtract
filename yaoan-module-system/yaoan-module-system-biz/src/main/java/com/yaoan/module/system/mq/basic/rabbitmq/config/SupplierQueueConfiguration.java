package com.yaoan.module.system.mq.basic.rabbitmq.config;

import com.yaoan.module.system.mq.basic.MqConst;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author doujiale
 * @description 供应商 rabbitMQ 队列配置
 */
@Configuration
public class SupplierQueueConfiguration {

    /**
     * 供应商注册信息同步
     *
     * @return Queue
     */
    @Bean(name = "gpxBasicGpbsSupplierRegisterQueue")
    public Queue getGpxBasicGpbsSupplierRegisterQueue() {
        return new Queue(MqConst.RabbitMq.SupplierQueue.REGISTER.getCode());
    }

    /**
     * 供应商删除信息同步
     *
     * @return Queue
     */
    @Bean(name = "gpxBasicGpbsSupplierRemoveQueue")
    public Queue getGpxBasicGpbsSupplierRemoveQueue() {
        return new Queue(MqConst.RabbitMq.SupplierQueue.REMOVE.getCode());
    }

    /**
     * 供应商信息修改
     *
     * @return Queue
     */
    @Bean(name = "gpxBasicGpbsSupplierUpdateQueue")
    public Queue getGpxBasicGpbsSupplierUpateQueue() {
        return new Queue(MqConst.RabbitMq.SupplierQueue.UPDATE.getCode());
    }

    /**
     * 供应商注册用户
     *
     * @return Queue
     */
    @Bean(name = "gpxBasicGpbsSupplierUserQueue")
    public Queue getGpxBasicGpbsSupplierUserQueue() {
        return new Queue(MqConst.RabbitMq.SupplierQueue.USER_REGISTER.getCode());
    }

    /**
     * 供应商用户删除
     *
     * @return Queue
     */
    @Bean(name = "gpxBasicGpbsSupplierUserDeleteQueue")
    public Queue getGpxBasicGpbsSupplierUserrDeleteQueue() {
        return new Queue(MqConst.RabbitMq.SupplierQueue.USER_REMOVE.getCode());
    }

    /**
     * 供应商用户修改
     *
     * @return Queue
     */
    @Bean(name = "gpxBasicGpbsSupplierUserUpdateQueue")
    public Queue getGpxBasicGpbsSupplierUserUpdateQueue() {
        return new Queue(MqConst.RabbitMq.SupplierQueue.USER_UPDATE.getCode());
    }

}
