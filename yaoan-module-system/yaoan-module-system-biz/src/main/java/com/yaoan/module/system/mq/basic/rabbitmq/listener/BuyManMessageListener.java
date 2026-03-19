package com.yaoan.module.system.mq.basic.rabbitmq.listener;

import com.yaoan.module.system.mq.basic.MqConst;
import com.yaoan.module.system.mq.basic.task.BuyManTask;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 采购人单位消息监听
 * @author doujiale
 */
@Component
public class BuyManMessageListener {

    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    @Qualifier("mqConsumerTaskExecutor")
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = (ThreadPoolTaskExecutor) taskExecutor;
    }

    /**
     * 采购人单位注册同步
     *
     * @param message message
     */
    @RabbitListener(queuesToDeclare = @Queue(MqConst.RabbitMq.BuyManQueueConst.REGISTER_CONST))
    public void buyManRegisterConsumer(String message) {
        taskExecutor.submit(new BuyManTask.Register(message));
    }

    /**
     * 采购人单位信息变更
     *
     * @param message message
     */
    @RabbitListener(queuesToDeclare = @Queue(MqConst.RabbitMq.BuyManQueueConst.UPDATE_CONST))
    public void buyManUpdateConsumer(String message) {
        taskExecutor.submit(new BuyManTask.Update(message));
    }

    /**
     * 采购人人员注册同步
     *
     * @param message message
     */
    @RabbitListener(queuesToDeclare = @Queue(MqConst.RabbitMq.BuyManQueueConst.USER_REGISTER_CONST))
    public void buyManUserRegisterConsumer(String message) {
        taskExecutor.submit(new BuyManTask.UserRegister(message));
    }

    /**
     * 采购人 人员信息变更
     *
     * @param message message
     */
    @RabbitListener(queuesToDeclare = @Queue(MqConst.RabbitMq.BuyManQueueConst.USER_UPDATE_CONST))
    public void buyManUserUpdateConsumer(String message) {
        taskExecutor.submit(new BuyManTask.UserUpdate(message));
    }

}
