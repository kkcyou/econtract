package com.yaoan.module.econtract.controller.admin.bpm.archive.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 归档审批页面展示
 */
@Schema(description = "归档审批页面展示 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PageReqVO extends PageParam {
    /**
     * 档案名称
     */
    @Schema(description = "档案名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 档号
     */
    @Schema(description = "档号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    /**
     * 审批类型
     */
    @Schema(description = "审批类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer type;

    /**
     * 审批状态
     */
    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    /**
     * 申请人id
     */
    @Schema(description = "申请人id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String applicantId;
    /**
     * 申请人名字
     */
    @Schema(description = "申请人名字", requiredMode = Schema.RequiredMode.REQUIRED)
    private String applicantName;

    /**
     *  开始时间
     */
    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    /**
     *  结束时间
     */
    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    /**
     * {@link ApprovePageFlagEnums}
     * ALL(0, "全部"),
     * DONE(1, "已审批"),
     * TO_DO(2, "未审批"),
     */
    private Integer flag;

    /**
     * 流程实例
     */
    List<String> instanceIdList;
    /**
     * 搜索任务状态的字段
     */
    private Integer taskResult;
}
