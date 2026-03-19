package com.yaoan.module.system.mq.basic.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * rabbitmq，供应商队列
 *
 */
@Configuration
@Import(SupplierQueueConfiguration.class)
public class SupplierTopicExchangeAndBindingConfiguration {

    /**
     * 供应商业务交换机
     */
    @Bean(name = "gpbsSupplierExchange")
    TopicExchange getGpbsSupplierExchange() {
        return new TopicExchange("gpbs.supplier");
    }

    /**
     * 定义队列监听供应商注册
     *
     */
    @Bean
    Binding bindingQueueOneToTopicExchange(
            @Qualifier("gpxBasicGpbsSupplierRegisterQueue") Queue gpxBasicGpbsSupplierRegisterQueue,
            @Qualifier("gpbsSupplierExchange") TopicExchange gpbsSupplierExchange) {
        return BindingBuilder.bind(gpxBasicGpbsSupplierRegisterQueue)
                .to(gpbsSupplierExchange)
                .with("register.create");
    }

    /**
     * 定义队列监听供应商删除
     *
     */
    @Bean
    Binding bindingRemoveQueueToTopicExchange(
            @Qualifier("gpxBasicGpbsSupplierRemoveQueue") Queue gpxBasicGpbsSupplierRemoveQueue,
            @Qualifier("gpbsSupplierExchange") TopicExchange gpbsSupplierExchange) {
        return BindingBuilder.bind(gpxBasicGpbsSupplierRemoveQueue)
                .to(gpbsSupplierExchange)
                .with("register.remove");
    }

    /**
     * 供应商信息修改
     *
     * @param gpxBasicGpbsSupplierUpdateQueue
     * @param gpbsSupplierExchange
     * @return
     */
    @Bean
    Binding bindingUpdateQueueToTopicExchange(
            @Qualifier("gpxBasicGpbsSupplierUpdateQueue") Queue gpxBasicGpbsSupplierUpdateQueue,
            @Qualifier("gpbsSupplierExchange") TopicExchange gpbsSupplierExchange) {
        return BindingBuilder.bind(gpxBasicGpbsSupplierUpdateQueue)
                .to(gpbsSupplierExchange)
                .with("register.update");
    }

    /**
     * 供应商用户
     *
     * @param gpxBasicGpbsSupplierUserQueue
     * @param gpbsSupplierExchange
     * @return Binding
     */
    @Bean
    Binding bindingUserQueueToTopicExchange(
            @Qualifier("gpxBasicGpbsSupplierUserQueue") Queue gpxBasicGpbsSupplierUserQueue,
            @Qualifier("gpbsSupplierExchange") TopicExchange gpbsSupplierExchange) {
        return BindingBuilder.bind(gpxBasicGpbsSupplierUserQueue)
                .to(gpbsSupplierExchange)
                .with("user.create");
    }

    /**
     * 供应商用户删除
     *
     * @param gpxBasicGpbsSupplierUserDeleteQueue
     * @param gpbsSupplierExchange
     * @return
     */
    @Bean
    Binding bindingUserDeleteQueueToTopicExchange(
            @Qualifier("gpxBasicGpbsSupplierUserDeleteQueue") Queue gpxBasicGpbsSupplierUserDeleteQueue,
            @Qualifier("gpbsSupplierExchange") TopicExchange gpbsSupplierExchange) {
        return BindingBuilder.bind(gpxBasicGpbsSupplierUserDeleteQueue)
                .to(gpbsSupplierExchange)
                .with("user.delete");
    }

    /**
     * 供应商用户修改
     *
     * @param gpxBasicGpbsSupplierUserUpdateQueue
     * @param gpbsSupplierExchange
     * @return
     */
    @Bean
    Binding bindingUserUpdateQueueToTopicExchange(
            @Qualifier("gpxBasicGpbsSupplierUserUpdateQueue") Queue gpxBasicGpbsSupplierUserUpdateQueue,
            @Qualifier("gpbsSupplierExchange") TopicExchange gpbsSupplierExchange) {
        return BindingBuilder.bind(gpxBasicGpbsSupplierUserUpdateQueue)
                .to(gpbsSupplierExchange)
                .with("user.update");
    }

}
