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
 * @author doujiale
 * @Description: 超管、多区划管理员业务交换机绑定队列配置
 * @Version:1.0
 **/
@Configuration
@Import(MultiRegionManagerConfiguration.class)
public class MultiRegionManagerTopicExchangeAndBindingConfiguration {

    /**
     * 超管、多区划管理员业务交换机
     */
    @Bean(name = "gpMenuCenterExchange")
    TopicExchange getGpMenuCenterExchange() {
        return new TopicExchange("gp.menu.center");
    }


    /**
     * 定义队列超管、多区划管理员信息注册
     */
    @Bean
    Binding bindingGpMenuCenterManagerRegisteToTopicExchange(
            @Qualifier("gpMenuCenterManagerRegisterQueue") Queue gpMenuCenterManagerRegisterQueue,
            @Qualifier("gpMenuCenterExchange") TopicExchange gpMenuCenterExchange) {
        return BindingBuilder.bind(gpMenuCenterManagerRegisterQueue)
                .to(gpMenuCenterExchange)
                .with("implementer.insert");
    }


    /**
     * 定义队列超管、多区划管理员信息更新
     */
    @Bean
    Binding bindingGpMenuCenterManagerUpdateQueueToTopicExchange(
            @Qualifier("gpMenuCenterManagerUpdateQueue") Queue gpMenuCenterManagerUpdateQueue,
            @Qualifier("gpMenuCenterExchange") TopicExchange gpMenuCenterExchange) {
        return BindingBuilder.bind(gpMenuCenterManagerUpdateQueue)
                .to(gpMenuCenterExchange)
                .with("implementer.update");
    }

}
