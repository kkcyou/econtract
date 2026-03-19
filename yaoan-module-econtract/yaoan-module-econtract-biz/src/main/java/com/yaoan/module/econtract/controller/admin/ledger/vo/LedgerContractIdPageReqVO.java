package com.yaoan.module.econtract.controller.admin.ledger.vo;

import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/19 17:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerContractIdPageReqVO extends PageParam {

    /**
     * 合同id
     */
    @NotNull(message = "合同id不可为空")
    private String contractId;

}
