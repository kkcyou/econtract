package com.yaoan.module.system.mq.basic.task;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.system.mq.basic.handler.SupervisionMessageHandler;

import java.util.concurrent.Callable;

/**
 * @author doujiale
 * @description 监管用户修改线程执行逻辑
 * @Version:1.0
 **/
public class SupervisionUserUpdateTask implements Callable<Boolean> {

    private String message;

    public SupervisionUserUpdateTask() {
    }

    public SupervisionUserUpdateTask(String message) {
        this.message = message;
    }

    @Override
    public Boolean call() throws Exception {
        SupervisionMessageHandler supervisionMessageHandler = SpringUtil.getBean(SupervisionMessageHandler.class);
        return supervisionMessageHandler.supervisionUserUpdateConsumer(message);
    }
}
