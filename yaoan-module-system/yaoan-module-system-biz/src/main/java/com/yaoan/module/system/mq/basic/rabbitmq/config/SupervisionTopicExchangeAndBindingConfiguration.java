package com.yaoan.module.system.mq.basic.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq，监管队列
 *
 */
@Configuration
public class SupervisionTopicExchangeAndBindingConfiguration {

    /**
     * 监管用户新增业务交换机
     */
    @Bean(name = "gpmsTopicExchange")
    TopicExchange gpmsTopicExchange() {
        return new TopicExchange("gpmsTopicExchange");
    }
    

    /**
     * 监管用户新增
     *
     * @param gpxBasicGpbsSupervisionUserQueue
     * @return
     */
    @Bean
    Binding bindingSupervisionUserQueueToTopicExchange(
            @Qualifier("gpmallBasicGpbsSupervisionUserQueue") Queue gpxBasicGpbsSupervisionUserQueue,
            @Qualifier("gpmsTopicExchange") TopicExchange gpmsTopicExchange) {
        return BindingBuilder.bind(gpxBasicGpbsSupervisionUserQueue)
                .to(gpmsTopicExchange)
                .with("gpmsKeyUserNew");
    }


    /**
     * 监管用户删除
     *
     * @param gpxBasicGpbsSupervisionUserDeleteQueue
     * @param gpmsTopicExchange
     * @return
     */
    @Bean
    Binding bindingSupervisionUserDeleteQueueToTopicExchange(
            @Qualifier("gpmallBasicGpbsSupervisionUserDeleteQueue") Queue gpxBasicGpbsSupervisionUserDeleteQueue,
            @Qualifier("gpmsTopicExchange") TopicExchange gpmsTopicExchange) {
        return BindingBuilder.bind(gpxBasicGpbsSupervisionUserDeleteQueue)
                .to(gpmsTopicExchange)
                .with("gpmsKeyUserDelete");
    }

    /**
     * 监管用户修改
     *
     * @param gpxBasicGpbsSupervisionUserUpdateQueue
     * @param gpmsTopicExchange
     * @return
     */
    @Bean
    Binding bindingSupervisionUserUpdateQueueToTopicExchange(
            @Qualifier("gpmallBasicGpbsSupervisionUserUpdateQueue") Queue gpxBasicGpbsSupervisionUserUpdateQueue,
            @Qualifier("gpmsTopicExchange") TopicExchange gpmsTopicExchange) {
        return BindingBuilder.bind(gpxBasicGpbsSupervisionUserUpdateQueue)
                .to(gpmsTopicExchange)
                .with("gpmsKeyUserUpdate");
    }

}
