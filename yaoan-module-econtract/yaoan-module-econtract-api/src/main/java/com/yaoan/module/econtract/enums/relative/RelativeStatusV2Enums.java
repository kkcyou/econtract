package com.yaoan.module.econtract.enums.relative;

import lombok.Getter;

/**
 * @description: 正常0 ，移入中1 ，黑名单2，移出中3
 * @author: Pele
 * @date: 2025/3/12 17:41
 */
@Getter
public enum RelativeStatusV2Enums {
    /**
     * 订单状态 枚举类
     */
    NORMAL("0", "通过"),

    MOVING_IN("1", "移入中"),

    BLACKLIST("2", "黑名单"),

    MOVING_OUT("3", "移出中"),

    DRAFTING("4", "草稿"),

    APPROVING("5", "审批中"),

    ;

    private final String code;
    private final String info;

    RelativeStatusV2Enums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static RelativeStatusV2Enums getInstance(String code) {
        for (RelativeStatusV2Enums value : RelativeStatusV2Enums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
