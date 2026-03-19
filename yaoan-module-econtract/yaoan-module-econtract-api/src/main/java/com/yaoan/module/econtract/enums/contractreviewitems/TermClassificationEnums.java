package com.yaoan.module.econtract.enums.contractreviewitems;

import lombok.Getter;


@Getter
public enum TermClassificationEnums {
    /**
     * 条款分类枚举
     */
    PARTIES(50, "合同主体"),
    SUBJECT_MATTER(100, "合同标的"),
    PRICE(150, "合同价款"),
    DELIVERY(200, "交付与验收条款"),
    PERFORMANCE(250, "履行期限、地点、方式"),
    EXECUTION(300, "合同签署条款"),
    LIABILITY(350, "违约责任"),
    CONFIDENTIALITY(400, "保密条款"),
    DISPUTE_RESOLUTION(450, "争议解决"),
    FORCE_MAJEURE(500, "不可抗力"),
    MISCELLANEOUS(550, "其他");




    private final Integer code;
    private final String info;

    TermClassificationEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static TermClassificationEnums getInstance(Integer code) {
        for (TermClassificationEnums value : TermClassificationEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
