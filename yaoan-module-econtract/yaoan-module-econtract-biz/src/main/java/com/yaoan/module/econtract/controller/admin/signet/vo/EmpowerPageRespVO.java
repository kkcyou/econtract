package com.yaoan.module.econtract.controller.admin.signet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;


@Data
@ToString(callSuper = true)
public class EmpowerPageRespVO {

    /**
     * 成员id
     */
    @Schema(description = "成员id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long empowerObjectId;

    /**
     * 成员名称
     */
    @Schema(description = "成员名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String empowerObjectName;

    /**
     * 部门id
     */
    @Schema(description = "部门id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long deptId;

    /**
     * 成员部门
     */
    @Schema(description = "成员部门", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String deptName;

    /**
     * 被授权印章数（个）
     */
    @Schema(description = "被授权印章数（个）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer count;
}
