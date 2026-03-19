package com.yaoan.module.system.enums.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/22 18:24
 */
@Getter
@AllArgsConstructor
public enum ConfigIfApproveEnums {
    /**
     * 是否审批 枚举类
     */
    NO_APPROVE("n", "不用审批", false),
    APPROVE("y", "走审批流", true),


    ;

    private final String code;
    private final String info;
    private final Boolean result;


    public static ConfigIfApproveEnums getInstance(String code) {
        for (ConfigIfApproveEnums value : ConfigIfApproveEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
