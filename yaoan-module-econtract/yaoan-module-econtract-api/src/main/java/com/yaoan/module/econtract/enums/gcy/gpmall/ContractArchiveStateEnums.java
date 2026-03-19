package com.yaoan.module.econtract.enums.gcy.gpmall;

import lombok.Getter;

/**
 * 备案状态--监管
 *
 * @author zhc
 * @version 1.0
 * @date 2024-04-24
 */
@Getter
public enum ContractArchiveStateEnums {

    /**
     * 被退回
     */
    UNSYNCHRONIZED(-3, "未同步合同信息"),
    WAIT_BAK(-2,"合同待备案"),
    BACING(-1,"合同备案中"),
    BAKED(0, "合同已备案"),
    BAK_FAIL(1, "合同备案失败");


    private final Integer code;
    private final String info;

    ContractArchiveStateEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ContractArchiveStateEnums getInstance(Integer code) {
        for (ContractArchiveStateEnums value : ContractArchiveStateEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
