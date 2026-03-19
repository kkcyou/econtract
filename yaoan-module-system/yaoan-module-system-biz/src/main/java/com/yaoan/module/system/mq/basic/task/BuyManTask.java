package com.yaoan.module.system.mq.basic.task;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.system.mq.basic.handler.BuyManMessageHandler;

import java.util.concurrent.Callable;

/**
 * @author doujiale
 * @program econtract-basic
 * @description 采购人同步任务
 */
public interface BuyManTask {

    /**
     * 采购人注册操作
     */
    class Register implements Callable<Boolean> {

        private final String message;

        public Register(String message) {
            this.message = message;
        }

        @Override
        public Boolean call() throws Exception {
            return SpringUtil.getBean(BuyManMessageHandler.class).buyManRegisterConsumer(message);
        }
    }

    /**
     * 采购人更新操作
     */
    class Update implements Callable<Boolean> {

        private final String message;

        public Update(String message) {
            this.message = message;
        }

        @Override
        public Boolean call() throws Exception {
            return SpringUtil.getBean(BuyManMessageHandler.class).buyManUpdateConsumer(message);
        }
    }

    /**
     * 采购人用户注册操作
     */
    class UserRegister implements Callable<Boolean> {

        private final String message;

        public UserRegister(String message) {
            this.message = message;
        }

        @Override
        public Boolean call() throws Exception {
            return SpringUtil.getBean(BuyManMessageHandler.class).buyManUserRegisterConsumer(message);
        }
    }

    /**
     * 采购人用户更新操作
     */
    class UserUpdate implements Callable<Boolean> {

        private final String message;

        public UserUpdate(String message) {
            this.message = message;
        }

        @Override
        public Boolean call() throws Exception {
            return SpringUtil.getBean(BuyManMessageHandler.class).buyManUserUpdateConsumer(message);
        }
    }
}
