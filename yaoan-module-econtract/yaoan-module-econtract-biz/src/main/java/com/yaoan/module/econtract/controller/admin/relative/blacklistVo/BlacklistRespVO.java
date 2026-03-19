package com.yaoan.module.econtract.controller.admin.relative.blacklistVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Schema(description = "相对方返回信息")
@ToString(callSuper = true)
public class BlacklistRespVO {
    /**
     * id
     */
    private String id;

    @Schema(description = "相对方id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String relativeId;

    @Schema(description = "附件id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long fileId;

    @Schema(description = "附件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;

    @Schema(description = "申请状态 申请0  审批1 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer applyStatus;
    @Schema(description = "申请状态名称 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String applyStatusName;
    @Schema(description = "申请类型 移入0 移出1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer applyType;

    @Schema(description = "申请意见", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String applyMsg;

    @Schema(description = "审批类型 审批0 拒绝1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer auditType;

    @Schema(description = "审批类型名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer auditTypeName;

    @Schema(description = "审批意见", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String auditMsg;
    private String creator;
    @Schema(description = "创建人姓名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String creatorName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @Schema(description = "相对方编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    //相对方名称
    @Schema(description = "相对方名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "相对方状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String status;
    @Schema(description = "相对方状态名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String statusName;
    //相对方类型 供应商0 客户1
    @Schema(description = "相对方类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer relativeType;
    //相对方性质  企业2  个人：1  单位3
    @Schema(description = "相对方性质", requiredMode = Schema.RequiredMode.REQUIRED)
    private String entityType;
    @Schema(description = "证件类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer cardType;
    @Schema(description = "证件号码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String cardNo;
    @Schema(description = "证件类型名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String cardTypeName;
    @Schema(description = "相对方来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sourceType;
    @Schema(description = "相对方等级", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String levelNo;
    private String sourceTypeName;
    private String relativeTypeName;
    private String entityTypeName;

    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptName;
}
