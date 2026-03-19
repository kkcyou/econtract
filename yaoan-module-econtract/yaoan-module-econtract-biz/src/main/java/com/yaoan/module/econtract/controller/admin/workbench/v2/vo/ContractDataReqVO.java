package com.yaoan.module.econtract.controller.admin.workbench.v2.vo;

import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2025/2/11 10:56
 */
@Data
public class ContractDataReqVO extends PageParam {

    /**
     * contract,seal,pay,collection
     */
    @NotNull(message = "flag不可为空")
    private String flag;
}
