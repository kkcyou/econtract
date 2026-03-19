package com.yaoan.module.system.mq.basic.task;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.system.mq.basic.handler.SupervisionMessageHandler;

import java.util.concurrent.Callable;

/**
 * @program: gpem-interface
 * @description 监管用户删除线程执行逻辑
 * @author Linzd
 * @date 2021-04-19 20:54
 * @Version:1.0
 **/
public class SupervisionUserDeleteTask implements Callable<Boolean> {

    private String message;

    public SupervisionUserDeleteTask() {
    }

    public SupervisionUserDeleteTask(String message) {
        this.message = message;
    }

    @Override
    public Boolean call() throws Exception {
        SupervisionMessageHandler supervisionMessageHandler = SpringUtil.getBean(SupervisionMessageHandler.class);

        return supervisionMessageHandler.supervisionUserDeleteConsumer(message);
    }
}
