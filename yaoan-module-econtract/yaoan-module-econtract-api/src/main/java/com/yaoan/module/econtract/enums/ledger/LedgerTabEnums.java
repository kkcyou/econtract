package com.yaoan.module.econtract.enums.ledger;

import com.yaoan.module.econtract.enums.neimeng.AttachmentTypeEnums;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2025-5-19 15:26
 */
@Getter
public enum LedgerTabEnums {


    APPLICATION("application", "申请信息"),
    TEXT("text", "正文&附件"),
    SIGN("sign", "合同签订"),
    CHANGE("change", "变动记录"),
    PAY("pay", "付款记录"),
    COLLECT("collect ", "收款记录"),
    ACCEPTANCE("acceptance", "验收记录"),
    BORROW("borrow", "借阅记录"),

    ;

    private final String code;
    private final String info;

    LedgerTabEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static LedgerTabEnums getInstance(String code) {
        for (LedgerTabEnums value : LedgerTabEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
