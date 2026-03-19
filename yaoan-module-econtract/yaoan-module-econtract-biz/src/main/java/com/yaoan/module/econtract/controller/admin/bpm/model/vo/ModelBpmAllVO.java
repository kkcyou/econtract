package com.yaoan.module.econtract.controller.admin.bpm.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/13 21:03
 */
@Data
public class ModelBpmAllVO extends TenantBaseDO {

    private static final long serialVersionUID = -3876075156229120785L;

    @Schema(description = "申请信息标识")
    private String id;

    @Schema(description = "任务标识")
    private String taskId;

    @Schema(description = "流程实例的编号")
    private String processInstanceId;

    @Schema(description = "提交人名称")
    private String userName;

    @Schema(description = "审批状态Str")
    private String resultStr;

    /**
     * 流程发起人
     */
    private Long userId;

    /**
     * 模板id
     */
    private String modelId;

    /**
     * 模板编码
     */
    private String code;

    /**
     * 模板名称
     */
    private String modelName;

    /**
     * 审批类型
     */
    private String approveType;

    /**
     * 审批结果
     */
    private Integer result;



    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 审批时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime approveTime;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime submitTime;
}
