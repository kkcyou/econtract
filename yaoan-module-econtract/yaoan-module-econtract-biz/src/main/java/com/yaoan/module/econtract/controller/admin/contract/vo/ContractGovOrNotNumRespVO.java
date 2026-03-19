package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Schema(description = "Contract GovOrNotNumRequest VO")
@Data
public class ContractGovOrNotNumRespVO {
    private static final long serialVersionUID = -4802787786314022100L;
    @Schema(description = "政采合同数量")
    private long govContractNum;
    @Schema(description = "非政采合同数量")
    private long notGovContractNum;
}
