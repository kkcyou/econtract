package com.yaoan.module.system.mq.basic.task;


import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.system.mq.basic.handler.SupervisionMessageHandler;

import java.util.concurrent.Callable;

/**
 * @program: gpem-interface
 * @description 监管用户新增线程执行逻辑
 * @Version:1.0
 **/
public class SupervisionUserNewTask implements Callable<Boolean> {

    private String message;

    public SupervisionUserNewTask() {
    }

    public SupervisionUserNewTask(String message) {
        this.message = message;
    }

    @Override
    public Boolean call() throws Exception {
        SupervisionMessageHandler supervisionMessageHandler = SpringUtil.getBean(SupervisionMessageHandler.class);
        return supervisionMessageHandler.supervisionUserNewConsumer(message);
    }
}
