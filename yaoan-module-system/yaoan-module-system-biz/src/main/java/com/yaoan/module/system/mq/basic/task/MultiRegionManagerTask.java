package com.yaoan.module.system.mq.basic.task;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.system.mq.basic.handler.MultiRegionManagerMessageHandler;

import java.util.concurrent.Callable;

/**
 * @program gpmall-basic
 * @description 管理员 同步任务集合
 * @create 2022-11-09 09:37
 */
public interface MultiRegionManagerTask {

    /**
     * 区划管理员 任务
     */
    class Register implements Callable<Boolean> {
        private final String message;

        public Register(String message) {

            this.message = message;
        }

        @Override
        public Boolean call() throws Exception {
            return SpringUtil.getBean(MultiRegionManagerMessageHandler.class).multiZoneManagerRegisterConsumer(message);
        }
    }

    /**
     * 区划管理员 更新任务
     */
    class Update implements Callable<Boolean> {
        private final String message;

        public Update(String message) {
            this.message = message;
        }

        @Override
        public Boolean call() throws Exception {
            return SpringUtil.getBean(MultiRegionManagerMessageHandler.class).multiZoneManagerUpdateConsumer(message);
        }
    }

}
