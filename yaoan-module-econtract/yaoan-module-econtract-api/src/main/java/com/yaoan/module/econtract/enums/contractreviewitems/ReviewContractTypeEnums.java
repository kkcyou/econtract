package com.yaoan.module.econtract.enums.contractreviewitems;

import lombok.Getter;


/**
 * @author wsh
 */

@Getter
public enum ReviewContractTypeEnums {
    /**
     * 规则合同类型
     */
    // 买卖合同
    SALES_CONTRACT(1, "买卖合同"),
    // 招投标买卖合同
    BIDDING_SALES_CONTRACT(2, "招投标买卖合同"),
    // 租赁合同
    LEASE_CONTRACT(3, "租赁合同"),
    // 建设工程合同
    CONSTRUCTION_CONTRACT(4, "建设工程合同"),
    // 承揽合同
    CONTRACT_FOR_WORK(5, "承揽合同"),
    // 融资租赁合同
    FINANCIAL_LEASE_CONTRACT(6, "融资租赁合同"),
    // 保证合同
    GUARANTEE_CONTRACT(7, "保证合同"),
    // 劳动合同
    LABOR_CONTRACT(8, "劳动合同"),
    // 运输合同
    TRANSPORT_CONTRACT(9, "运输合同"),
    // 技术合同
    TECHNOLOGY_CONTRACT(10, "技术合同"),
    // 公用事业服务合同
    PUBLIC_UTILITY_CONTRACT(11, "公用事业服务合同");


    ;

    private final Integer code;
    private final String info;

    ReviewContractTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ReviewContractTypeEnums getInstance(Integer code) {
        for (ReviewContractTypeEnums value : ReviewContractTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
