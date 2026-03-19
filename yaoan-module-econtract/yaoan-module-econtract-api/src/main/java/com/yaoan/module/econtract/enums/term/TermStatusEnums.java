package com.yaoan.module.econtract.enums.term;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/5 10:00
 */
@Getter
@AllArgsConstructor
public enum TermStatusEnums {
    /**
     * 条款发布状态
     */
    NO("0", "未发布/停用"),
    YES("1", "已发布/启用"),
    ;

    private final String code;
    private final String desc;


}
