package com.yaoan.module.system.mq.basic.rabbitmq.listener;

import com.yaoan.module.system.mq.basic.MqConst;
import com.yaoan.module.system.mq.basic.task.SupplierTask;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author doujiale
 * @program econtract-basic
 * @description 注入 消息监听器
 **/
@Component
public class SupplierMessageListener {

    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    @Qualifier("mqConsumerTaskExecutor")
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = (ThreadPoolTaskExecutor) taskExecutor;
    }

    /**
     * 供应商注册同步
     *
     * @param message message
     */
    @RabbitListener(queuesToDeclare = @Queue(MqConst.RabbitMq.SupplierQueueConst.REGISTER_CONST))
    public void supplierRegisterConsumer(String message) {
        taskExecutor.submit(new SupplierTask.Register(message));
    }

    /**
     * 供应商单位信息修改
     *
     * @param message message
     */
    @RabbitListener(queuesToDeclare = @Queue(MqConst.RabbitMq.SupplierQueueConst.UPDATE_CONST))
    public void supplierUpdateConsumer(String message) {
        taskExecutor.submit(new SupplierTask.Update(message));
    }

    /**
     * 供应商用户信息新增
     *
     * @param message message
     */
    @RabbitListener(queuesToDeclare = @Queue(MqConst.RabbitMq.SupplierQueueConst.USER_REGISTER_CONST))
    public void supplierUserRegisterConsumer(String message) {
        taskExecutor.submit(new SupplierTask.UserRegister(message));
    }

    /**
     * 供应商用户信息修改
     *
     * @param message message
     */
    @RabbitListener(queuesToDeclare = @Queue(MqConst.RabbitMq.SupplierQueueConst.USER_UPDATE_CONST))
    public void supplierUserUpdConsumer(String message) {
        taskExecutor.submit(new SupplierTask.UserUpdate(message));
    }
}
