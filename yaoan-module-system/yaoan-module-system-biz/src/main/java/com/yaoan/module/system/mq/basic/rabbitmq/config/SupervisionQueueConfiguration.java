package com.yaoan.module.system.mq.basic.rabbitmq.config;

import com.yaoan.module.system.mq.basic.MqConst;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author doujiale
 * @description
 */
@Configuration
public class SupervisionQueueConfiguration {


    /**
     * 监管注册用户
     */
    @Bean(name = "gpmallBasicGpbsSupervisionUserQueue")
    public Queue getGpxBasicGpbsSupervisionUserQueue() {
        return new Queue(MqConst.SupervisionQueue.userRegisterQueue);
    }

    /**
     * 监管用户删除
     */
    @Bean(name = "gpmallBasicGpbsSupervisionUserDeleteQueue")
    public Queue getGpxBasicGpbsSupervisionUserrDeleteQueue() {
        return new Queue(MqConst.SupervisionQueue.userDeleteQueue);
    }

    /**
     * 监管用户修改
     */
    @Bean(name = "gpmallBasicGpbsSupervisionUserUpdateQueue")
    public Queue getGpxBasicGpbsSupervisionUserUpdateQueue() {
        return new Queue(MqConst.SupervisionQueue.userUpdateQueue);
    }

}
