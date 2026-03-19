package com.yaoan.module.system.enums.notify;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知模板类型枚举
 *
 * @author HUIHUI
 */
@Getter
@AllArgsConstructor
public enum NotifyTemplateTypeEnum {

    /**
     * 系统消息
     */
    SYSTEM_MESSAGE(2),
    /**
     * 通知消息
     */
    NOTIFICATION_MESSAGE(1),
    /**
     * 预警提醒消息
     */
    WARNING_MESSAGE(3);
    private final Integer type;

}
