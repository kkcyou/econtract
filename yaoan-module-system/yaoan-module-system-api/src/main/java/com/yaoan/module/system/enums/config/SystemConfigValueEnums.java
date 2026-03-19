package com.yaoan.module.system.enums.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/27 14:03
 */
@Getter
@AllArgsConstructor
public enum SystemConfigValueEnums {
    /**
     * 系统动态配置 值的 枚举类
     */

    APPROVER_PERMISSION_SCAN_ANNOTATION_ALL("approver_permission_scan_annotation","all","审批人的浏览批注的权限:全部"),
    APPROVER_PERMISSION_SCAN_ANNOTATION_LIMITED("approver_permission_scan_annotation","self_and_next_approver","审批人的浏览批注的权限：自己和下一个节点审批人"),


    ;

    private final String key;
    private final String value;
    private final String info;




    public static SystemConfigValueEnums getInstance(String key) {
        for (SystemConfigValueEnums value : SystemConfigValueEnums.values()) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }
        return null;
    }
}
