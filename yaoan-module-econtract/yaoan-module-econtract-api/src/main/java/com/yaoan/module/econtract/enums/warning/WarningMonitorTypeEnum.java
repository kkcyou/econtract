package com.yaoan.module.econtract.enums.warning;

import lombok.Getter;

@Getter
public enum WarningMonitorTypeEnum {
    BUSINESS(1, "业务数据"),
    WORK_FLOW(2, "流程数据"),
    COMPLEX_LOGIC(3, "复杂逻辑监控项"); //单独处理逻辑，代码中实现

    private final Integer code;
    private final String desc;

    WarningMonitorTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static WarningMonitorTypeEnum getInstance(Integer code) {
        for (WarningMonitorTypeEnum value : WarningMonitorTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
