package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
public class EmpowerObject {
    /**
     * 授权id
     */
    @Schema(description = "授权id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

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
     * 授权范围
     */
    @Schema(description = "授权范围", requiredMode = Schema.RequiredMode.REQUIRED)
    private String empowerRange;

    /**
     * 是否长期有效（0：否，1：是）
     */
    @Schema(description = "是否长期有效", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer empowerIsPermanent;


    /**
     * 授权开始时间
     */
    @Schema(description = "授权开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date empowerStartDate;

    /**
     * 授权结束时间
     */
    @Schema(description = "授权结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date empowerEndDate;

    /**
     * 授权人
     */
    @Schema(description = "授权人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String licensor;

    /**
     * 授权时间
     */
    @Schema(description = "授权时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date empowerDate;

    /**
     *  授权状态（0：失效，1：正常）
     */
    @Schema(description = " 授权状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer empowerStatus;
    /**
     *  授权状态名称
     */
    @Schema(description = " 授权状态名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String empowerStatusName;

    /**
     * 用印文件数
     */
    @Schema(description = "用印文件数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long empowerCount;
}
