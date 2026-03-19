package com.yaoan.module.system.mq.basic.rabbitmq.config;

import com.yaoan.module.system.mq.basic.MqConst;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program gpem-basic
 * @description 实施运维租户配置
 **/
@Configuration
@ConditionalOnProperty(prefix = "gp.stream.client", name = "enabled", havingValue = "false", matchIfMissing = true)
public class MultiRegionManagerConfiguration {

    /**
     * 实施运维租户新增队列
     *
     * @return Queue
     */
    @Bean(name = "gpMenuCenterManagerRegisterQueue")
    public Queue getGpMenuCenterManagerRegisterQueue() {
        return new Queue(MqConst.RabbitMq.ManagerQueue.REGISTER.getCode());
    }

    /**
     * 实施运维租户跟更新队列
     *
     * @return Queue
     */
    @Bean(name = "gpMenuCenterManagerUpdateQueue")
    public Queue getGpMenuCenterManagerUpdateQueue() {
        return new Queue(MqConst.RabbitMq.ManagerQueue.UPDATE.getCode());
    }

}
