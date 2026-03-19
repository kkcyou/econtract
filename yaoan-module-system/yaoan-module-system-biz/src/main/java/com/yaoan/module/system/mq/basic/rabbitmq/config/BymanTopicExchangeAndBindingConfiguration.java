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
 * rabbitmq，监听采购人注册队列
 * @author doujiale
 */
@Configuration
@Import(BuyManQueueConfiguration.class)
public class BymanTopicExchangeAndBindingConfiguration {

    /**
     * 采购人业务交换机
     */
    @Bean(name = "gpbpBuyManExchange")
    TopicExchange getGpbpBuyManExchange() {
        return new TopicExchange("gpbpTopicExchange");
    }

    /**
     * 定义队列监听采购人单位信息注册
     */
    @Bean
    Binding bindingGpbpBuyManQueueToTopicExchange(
            @Qualifier("gpxBasicGpbpBuyManRegisterQueue") Queue gpxBasicGpbpBuyManRegisterQueue,
            @Qualifier("gpbpBuyManExchange") TopicExchange gpbpBuyManExchange) {
        return BindingBuilder.bind(gpxBasicGpbpBuyManRegisterQueue)
                .to(gpbpBuyManExchange)
                .with("gpbpKeyNew");
    }

    /**
     * 定义队列监听采购人单位信息变更
     *
     * @date 2019-04-01
     */
    @Bean
    Binding bindingGpbpBuyManUpdateQueueToTopicExchange(
            @Qualifier("gpxBasicGpbpBuyManUpdateQueue") Queue gpxBasicGpbpBuyManUpdateQueue,
            @Qualifier("gpbpBuyManExchange") TopicExchange gpbpBuyManExchange) {
        return BindingBuilder.bind(gpxBasicGpbpBuyManUpdateQueue)
                .to(gpbpBuyManExchange)
                .with("gpbpKeyUpdate");
    }

    /**
     * 定义队列监听采购人删除
     *
     * @date 2019-04-01
     */
    @Bean
    Binding bindingGpbpBuyManRemoveQueueToTopicExchange(
            @Qualifier("gpxBasicGpbpBuyManRemoveQueue") Queue gpxBasicGpbpBuyManRemoveQueue,
            @Qualifier("gpbpBuyManExchange") TopicExchange gpbpBuyManExchange) {
        return BindingBuilder.bind(gpxBasicGpbpBuyManRemoveQueue)
                .to(gpbpBuyManExchange)
                .with("gpbpKeyDelete");
    }

    /**
     * 定义队列监听采购人添加人员信息
     *
     * @date 2019-04-01
     */
    @Bean
    Binding bindingGpbpBuyManUserQueueToTopicExchange(
            @Qualifier("gpxBasicGpbpBuyManUserQueue") Queue gpxBasicGpbpBuyManRemoveQueue,
            @Qualifier("gpbpBuyManExchange") TopicExchange gpbpBuyManExchange) {
        return BindingBuilder.bind(gpxBasicGpbpBuyManRemoveQueue)
                .to(gpbpBuyManExchange)
                .with("gpbpKeyUserNew");
    }

    /**
     * 定义队列监听采购人变更人员信息
     *
     * @date 2019-04-01
     */
    @Bean
    Binding bindingGpbpBuyManUserUpdateQueueToTopicExchange(
            @Qualifier("gpxBasicGpbpBuyManUpdateUserQueue") Queue gpxBasicGpbpBuyManUpdateUserQueue,
            @Qualifier("gpbpBuyManExchange") TopicExchange gpbpBuyManExchange) {
        return BindingBuilder.bind(gpxBasicGpbpBuyManUpdateUserQueue)
                .to(gpbpBuyManExchange)
                .with("gpbpKeyUserUpdate");
    }
}
