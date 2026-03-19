package com.yaoan.module.econtract.controller.admin.ledger.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/19 17:24
 */
@Data
public class LedgerContractIdReqVO {

    /**
     * 合同id
     */
    @NotNull(message = "合同id不可为空")
    private String contractId;


}
