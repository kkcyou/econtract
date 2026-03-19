package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/13 11:55
 */
@Data
public class ContractTypeSignetReqVO {
    /**
     * 合同类型ID
     */
    @Schema(description = "合同类型ID")
    private String id;
    /**
     * 对应平台id
     */
    @Schema(description = "对应平台id")
    private String platId;
}
