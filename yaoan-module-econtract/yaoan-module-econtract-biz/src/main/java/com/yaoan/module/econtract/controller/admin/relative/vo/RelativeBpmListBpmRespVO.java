package com.yaoan.module.econtract.controller.admin.relative.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableParam;
import com.yaoan.module.econtract.enums.relative.RelativeStatusV2Enums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2025/3/11 19:51
 */
@Data
public class RelativeBpmListBpmRespVO extends FlowableParam {

    /**
     * 相对方id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "相对方编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    //相对方名称
    @Schema(description = "相对方名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "是否仅本租户使用，0 是 1否")
    private Integer tenantType;

    @Schema(description = "相对方用户id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long contactId;
    @Schema(description = "相对方用户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactName;
    @Schema(description = "相对方用户编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactAccount;
    //相对方类型 供应商1 客户2
    @Schema(description = "相对方类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer relativeType;
    @Schema(description = "相对方类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String relativeTypeName;
    //相对方性质  企业2  个人：1  单位3
    @Schema(description = "相对方性质", requiredMode = Schema.RequiredMode.REQUIRED)
    private String entityType;
    @Schema(description = "相对方性质", requiredMode = Schema.RequiredMode.REQUIRED)
    private String entityTypeName;
    //相对方来源 1新增 2导入
    @Schema(description = "相对方来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sourceType;
    @Schema(description = "相对方来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sourceTypeName;
    @Schema(description = "相对方等级", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String levelNo;

    //相对方状态 黑名单状态 正常0 ，移入中1 ，黑名单2，移出中3
    /**
     * {@link RelativeStatusV2Enums}
     * */
    @Schema(description = "相对方状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String status;
    public String statusName;

    @Schema(description = "开户名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankAccountName;

    @Schema(description = "银行账号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankAccount;
    @Schema(description = "开户行名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankName;
    private String creator;
    private String creatorName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 流程实例id
     * */
    private String processInstanceId;

    public String taskId;

    /**
     * 流程状态
     * */
    private Integer result;
    /**
     * 流程状态
     * */
    private String resultStr;
    /**
     * 审批状态
     */
    private Integer flowStatus;

    /**
     * 提交时间
     * */
    private LocalDateTime submitTime;

    /**
     * 审批时间
     * */
    private LocalDateTime approveTime;
}
