package com.yaoan.module.econtract.controller.admin.relation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 新增合同关联关系参数传递类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Data
@Schema(description = "合同关联关系基本vo")
public class BaseIncidenceRelationVO {
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractId;
    @Schema(description = "关联合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String relationContractId;
    @Schema(description = "关联关系", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer incidenceRelation;
}
