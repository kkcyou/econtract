package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 合同履约验收要求 vo--用于备案推送合同履约验收要求信息给监管备案
 * @author: zhc
 * @date: 2024-08-22
 */
@Data
public class ContractAcceptanceVo implements Serializable {

    private static final long serialVersionUID = -2760153109152440961L;
    /**
     * 验收组织方式
     */
    @Schema(description = "验收组织方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acceptanceType;
    /**acceptanceDate
     *验收主体---验收组织方式为委托第三方验收时该项必填
     */
    @Schema(description = "验收主体", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String performMainBody;
    /**
     * 是否邀请本项目其他供应商
     */
    @Schema(description = "是否邀请本项目其他供应商", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isInviteSupplier;
    /**
     * 是否邀请验收评审专家
     */
    @Schema(description = "是否邀请验收评审专家", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isInviteExpert;
    /**
     * 是否邀请服务对象
     */
    @Schema(description = "是否邀请服务对象", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isInviteServiceObject;
    /**
     * 是否邀请第三方检测机构
     */
    @Schema(description = "是否邀请第三方检测机构", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isProfessionalDetection;
    /**
     * 是否进行抽查检测(默认:0)
     */
    @Schema(description = "是否进行抽查检测", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isSpotCheck;
    /**
     * 抽查检测比例----是否进行抽查检测为1时该项必填
     */
    @Schema(description = "抽查检测比例", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double spotCheckProportion;
    /**
     * 是否存在破坏性检测(默认:0)
     */
    @Schema(description = "是否存在破坏性检测", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isDestructiveCheck;
    /**
     * 被破坏的检测产品的处理方式----是否存在破坏性检测为1时该项必填
     */
    @Schema(description = "被破坏的检测产品的处理方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String destructiveCheckMethod;
    /**
     * 组织验收的其他事项----非必填
     */
    @Schema(description = "组织验收的其他事项", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String otherPreparations;
    /**
     * 预计自供应商提出之日起N日内进行验收----与履约验收时间2选1
     */
    @Schema(description = "预计自供应商提出之日起N日内进行验收", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer infewdays;
    /**
     * 履约验收时间
     */
//    @JsonFormat( pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @Schema(description = "履约验收时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long acceptanceDate;
    /**
     * 验收方法
     */
    @Schema(description = "验收方法", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractAcpMethod;
    /**
     * 分期/分项验收工作安排---验收方法为分段验收或分期验收时该项必填
     */
    @Schema(description = "期/分项验收工作安排", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String acceptanceWorkArrange;
    /**
     * 验收内容
     */
    @Schema(description = "验收内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acceptanceContent;
    /**
     * 是否以采购活动中供应商提供的样品作为参考
     */
    @Schema(description = "是否以采购活动中供应商提供的样品作为参考", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isSampleReference;
    /**
     * 验收标准
     */
    @Schema(description = "验收标准", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acceptanceCriteria;
    /**
     * 履约验收程序
     */
    @Schema(description = "履约验收程序", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acceptanceProcedure;
    /**
     * 履约验收其他事项--非必填
     */
    @Schema(description = "履约验收其他事项", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String otherContent;
}
