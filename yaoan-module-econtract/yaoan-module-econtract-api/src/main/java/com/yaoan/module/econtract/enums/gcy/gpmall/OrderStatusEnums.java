package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * @description:  电子合同管理-统一订单状态
 * @author: wsh
 * @date: 2023/12/12 18:15
 */
@Getter
public enum OrderStatusEnums {

    /**
     * 订单状态 枚举类
     */
    FINISH("1", "已完成"),
    INITIATE_CANCEL("2", "发起取消"),
    CONFIRM_CANCEL("-1", "确认取消"),
    DELETE("-2", "删除"),
    NULLIFY("-3", "作废"),
    RETURNS("-4", "退货"),
    RESTORE_NORMAL("3", "恢复正常");

    private final String code;
    private final String info;

    OrderStatusEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static OrderStatusEnums getInstance(String code) {
        for (OrderStatusEnums value : OrderStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
