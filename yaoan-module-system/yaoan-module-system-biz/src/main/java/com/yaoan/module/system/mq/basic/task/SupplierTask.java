package com.yaoan.module.system.mq.basic.task;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.system.mq.basic.handler.SupplierMessageHandler;

import java.util.concurrent.Callable;

/**
 * @author doujiale
 * @program gpmall-basic
 * @description 供应商同步任务 集合
 */
public interface SupplierTask {

    /**
     * 供应商注册操作
     */
    class Register implements Callable<Boolean> {

        private final String message;

        public Register(String message) {
            this.message = message;
        }

        @Override
        public Boolean call() throws Exception {
            return SpringUtil.getBean(SupplierMessageHandler.class).supplierRegisterConsumer(message);
        }
    }

    /**
     * 供应商更新操作
     */
    class Update implements Callable<Boolean> {

        private final String message;

        public Update(String message) {
            this.message = message;
        }

        @Override
        public Boolean call() throws Exception {
            return SpringUtil.getBean(SupplierMessageHandler.class).supplierUpdateConsumer(message);
        }
    }

    /**
     * 供应商用户注册操作
     */
    class UserRegister implements Callable<Boolean> {

        private final String message;

        public UserRegister(String message) {
            this.message = message;
        }

        @Override
        public Boolean call() throws Exception {
            return SpringUtil.getBean(SupplierMessageHandler.class).supplierUserConsumer(message);
        }
    }

    /**
     * 供应商用户更新操作
     */
    class UserUpdate implements Callable<Boolean> {

        private final String message;

        public UserUpdate(String message) {
            this.message = message;
        }

        @Override
        public Boolean call() throws Exception {
            return SpringUtil.getBean(SupplierMessageHandler.class).supplierUserUpdateConsumer(message);
        }
    }
}
