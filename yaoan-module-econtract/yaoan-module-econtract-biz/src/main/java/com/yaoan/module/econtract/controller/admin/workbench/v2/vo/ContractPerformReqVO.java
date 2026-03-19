package com.yaoan.module.econtract.controller.admin.workbench.v2.vo;

import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2025/2/11 15:20
 */
@Data
public class ContractPerformReqVO extends PageParam {

    /**
     * 合同类型
     * */
    @NotNull(message = "合同类型不可为空")
    private String contractType;

    /**
     * 合同来源
     * 政采=1
     * 非政采=0
     * */
    @NotNull(message = "合同来源")
    private Integer source;
}
