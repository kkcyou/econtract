package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @description: 三方平台token配置枚举
 *
 */
@Getter
public enum BusinessTokenConfigEnums {
    /**
     * 第三方平台分类
     */
    PROJECT_PURCHASING(1, "project_config"),//项目采购
    FRAMEWORK_AGREEMENT(2, "agreement_config"),//框架采购协议
    ELECTRONICS_STORE(3, "store_config"),//电子卖场
    SUPER_CONTROL(4, "super_config"),//监管
    WARNING_USER(5, "warning_config");//预警

    private final Integer code;
    private final String info;

    BusinessTokenConfigEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static BusinessTokenConfigEnums getInstance(Integer code) {
        for (BusinessTokenConfigEnums value : BusinessTokenConfigEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
