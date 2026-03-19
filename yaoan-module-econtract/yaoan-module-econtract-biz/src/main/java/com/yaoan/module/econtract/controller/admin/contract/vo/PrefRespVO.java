package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

import java.util.List;

@Data
public class PrefRespVO {
    /**
     * 合同id
     */
    private String contractId;

    /**
     * 履约id
     */
    private String prefId;

    /**
     * 剩余时间
     */
    private String remainTime;

    /**
     * 需要展示的tab页
     *{@link com.yaoan.module.econtract.enums.ledger.LedgerTabEnums}
     * */
    private List<String> tabList;
}
