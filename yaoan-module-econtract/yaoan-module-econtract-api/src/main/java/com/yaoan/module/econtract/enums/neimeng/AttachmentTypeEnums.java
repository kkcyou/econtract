package com.yaoan.module.econtract.enums.neimeng;

import lombok.Getter;

/**
 * @description:
 * @author: doujl
 * @date: 2023/11/8 18:22
 */
@Getter
public enum AttachmentTypeEnums {
    /**
     * 合同附件类型
     * 此处该参数为：
     * 1.完整合同文本(ContractAtt-0020) (必备附件)。
     * 2.中小企业声明函(ContractAtt-0030) (非必备附件)。
     * 3.联合体协议书(ContractAtt-0040) (非必备附件)。
     * 4.其他附件(ContractAtt-9999) (非必备附件)。
     * 5.合同公示附件(去除涉密条款的合同文本)（ContractAtt-0080）
     */

    MAIN("ContractAtt-0020", "合同文本 - 必备附件"),
    STATEMENT("ContractAtt-0030", "中小企业声明函 - 非必备附件"),
    AGREEMENT("ContractAtt-0040", "联合体协议书 - 非必备附件"),
    OTHER("ContractAtt-9999", "其他附件"),
    PUBLIC("ContractAtt-0080", "合同公示附件(去除涉密条款的合同文本)"),
    ;

    private final String code;
    private final String info;

    AttachmentTypeEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static AttachmentTypeEnums getInstance(String code) {
        for (AttachmentTypeEnums value : AttachmentTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
