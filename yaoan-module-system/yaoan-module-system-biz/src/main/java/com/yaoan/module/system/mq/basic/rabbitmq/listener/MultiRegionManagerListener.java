package com.yaoan.module.system.mq.basic.rabbitmq.listener;

import com.yaoan.module.system.mq.basic.MqConst;
import com.yaoan.module.system.mq.basic.task.MultiRegionManagerTask;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author doujiale
 * @program gpmall-basic
 * @description 注入消息监听器监听队列
 **/

@Component
public class MultiRegionManagerListener {

    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    @Qualifier("mqConsumerTaskExecutor")
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = (ThreadPoolTaskExecutor) taskExecutor;
    }

    /**
     * 实施运维租户注册同步
     *
     * @param message message
     */
    @RabbitListener(queuesToDeclare = @Queue(MqConst.RabbitMq.ManagerQueueConst.REGISTER_CONST))
    public void multiZoneManagerRegisterConsumer(String message) {
        taskExecutor.submit(new MultiRegionManagerTask.Register(message));

    }

    /**
     * 实施运维租户新增同步
     *
     * @param message message
     */
    @RabbitListener(queuesToDeclare = @Queue(MqConst.RabbitMq.ManagerQueueConst.UPDATE_CONST))
    public void multiZoneManagerUpdateConsumer(String message) {
        taskExecutor.submit(new MultiRegionManagerTask.Update(message));
    }

}
