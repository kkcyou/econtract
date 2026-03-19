package com.yaoan.module.system.mq.basic.rabbitmq.listener;


import com.yaoan.module.system.mq.basic.MqConst;
import com.yaoan.module.system.mq.basic.task.SupervisionUserDeleteTask;
import com.yaoan.module.system.mq.basic.task.SupervisionUserNewTask;
import com.yaoan.module.system.mq.basic.task.SupervisionUserUpdateTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author doujiale
 * @Description 注入 消息监听器
 **/
@Component
@Slf4j
public class SupervisionMessageListener {


    @Autowired
    @Qualifier("mqConsumerTaskExecutor")
    private ThreadPoolTaskExecutor mqConsumerTaskExecutor;

   
    /**
     * @param message
     * @description 监管用户信息新增
     * @return void
     **/
    @RabbitListener(queuesToDeclare = @Queue(MqConst.SupervisionQueue.userRegisterQueue))
    public void supervisionUserAddConsumer(String message) {
        mqConsumerTaskExecutor.submit(new SupervisionUserNewTask(message));
    }
    

    /**
     * @param message
     * @description 监管用户信息修改
     * @return void
     **/
    @RabbitListener(queuesToDeclare = @Queue(MqConst.SupervisionQueue.userUpdateQueue))
    public void supervisionUserUpdConsumer(String message) {
        mqConsumerTaskExecutor.submit(new SupervisionUserUpdateTask(message));
    }

    /**
     * @param message
     * @description 监管用户信息删除
     * @return void
     **/
    @RabbitListener(queuesToDeclare = @Queue(MqConst.SupervisionQueue.userDeleteQueue))
    public void supervisionUserDeleteConsumer(String message) {
        mqConsumerTaskExecutor.submit(new SupervisionUserDeleteTask(message));
    }
}
