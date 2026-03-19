package com.yaoan.module.econtract.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 订单请求参数
 * @author: zhc
 * @date: 2024-04-24
 */
@Data
public class ContractArchiveStateRespDTO {
    private static final long serialVersionUID = 6056488516698227018L;
    /**
     * 合同ID
     */
    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractGuid;
    /**
     * 合同状态
     */
    @Schema(description = "合同备案状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer contractArchiveState;




}
