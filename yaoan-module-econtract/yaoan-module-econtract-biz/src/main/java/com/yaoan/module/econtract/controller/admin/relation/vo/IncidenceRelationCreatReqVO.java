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
@Schema(description = "新增合同关联关系")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class IncidenceRelationCreatReqVO extends BaseIncidenceRelationVO {
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractStatus;

}
