package com.yaoan.module.system.enums.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/27 13:48
 */
@Getter
@AllArgsConstructor
public enum AnnotationScanPermissionEnums {

    /**
     * 批注浏览权限枚举
     */
    APPROVER_SCAN("approver", "审批人的浏览批注的权限"),

    CARBON_COPY_SCAN("carbon_copy", "抄送人的浏览批注的权限"),

    ;

    private final String code;
    private final String info;


    public static AnnotationScanPermissionEnums getInstance(String code) {
        for (AnnotationScanPermissionEnums value : AnnotationScanPermissionEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
