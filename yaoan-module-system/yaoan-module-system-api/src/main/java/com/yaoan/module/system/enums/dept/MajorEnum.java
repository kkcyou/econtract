package com.yaoan.module.system.enums.dept;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公司类型的枚举值
 *
 * @author doujl
 */
@Getter
@AllArgsConstructor
public enum MajorEnum {

    /**
     * 单位
     */
    UNIT(3),
    /**
     * 企业
     */
    COMPANY(2),
    /**
     * 个人
     */
    PERSONAL(1);

    /**
     * 公司类型
     */
    private final Integer major;

}
