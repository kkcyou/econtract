package com.yaoan.module.econtract.controller.admin.signet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString(callSuper = true)
public class EmpowerObjectDetailsRespVO {

    /**
     * 授权对象id
     */
    @Schema(description = "授权对象id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long empowerObjectId;

    /**
     * 授权对象
     */
    @Schema(description = "授权对象", requiredMode = Schema.RequiredMode.REQUIRED)
    private String empowerObjectName;

    /**
     * 授权对象部门id
     */
    @Schema(description = "授权对象部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long deptId;

    /**
     * 授权对象部门
     */
    @Schema(description = "授权对象部门", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptName;

    /**
     * 成员联系方式
     */
    @Schema(description = "成员联系方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String mobile;
    /**
     * 被授权印章数
     */
    @Schema(description = "被授权印章数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer sealCount;

    /**
     * 授权对象列表
     */
    @Schema(description = "授权对象列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Signet> signetList;


}
