package com.yaoan.module.system.mq.basic.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Version:1.0
 **/
@Slf4j
@Service
public class MultiRegionManagerMessageHandler {

    /**
     * 实施运维租户 注册新增实施运维租户
     *
     * @param message message
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean multiZoneManagerRegisterConsumer(String message) {
        log.info("【RabbitMQ】【暂无逻辑执行】由队列取得新增实施运维租户消息：{}", message);
        return true;
    }

    /**
     * 实施运维租户 更新
     *
     * @param message message
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean multiZoneManagerUpdateConsumer(String message) {
        log.info("【RabbitMQ】【暂无逻辑执行】由队列取得更新实施运维租户消息：{}", message);
        return true;
    }


}
