package com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.api.gcy.order.GoodsVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.SignatoryRelReqVO;
import com.yaoan.module.econtract.enums.contract.ContractSourceTypeEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @description: 合同履约验收要求 vo--用于备案推送合同履约验收要求信息给监管备案
 * @author: zhc
 * @date: 2024-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderContractCreateReqV2Vo extends OrderContractCreateReqVO {

    private static final long serialVersionUID = -2760153109152440961L;

    /**
     * 供应商id
     */
    private String supplierId;

    /**
     * 代理机构id
     */
    private String agentId;
    /**
     * 采购单位id
     */
    private String buyerOrgId;

    /**
     * 验收组织方式
     */
    @Schema(description = "验收组织方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acceptanceType;
    /**
     * 验收主体---验收组织方式为委托第三方验收时该项必填
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
//    private Double spotCheckProportion;
    private String spotCheckProportion;
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
//    /**
//     * 预计自供应商提出之日起N日内进行验收----与履约验收时间2选1
//     */
//    @Schema(description = "预计自供应商提出之日起N日内进行验收", requiredMode = Schema.RequiredMode.REQUIRED)
//    private Integer infewdays;
    /**
     * 履约验收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(description = "履约验收时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date acceptanceDate;
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

    /**
     * 签署方id集合
     */
    @Schema(description = "签署方id集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SignatoryRelReqVO> signatoryList;

    /**
     * 订单ids
     */
    private List<String> orderIdList;

    /**
     * 标的信息
     */
    @Schema(description = "标的信息", requiredMode = Schema.RequiredMode.REQUIRED)
    List<GoodsVO> goodsVOS;


    /**
     * 来源类型
     *{@link ContractSourceTypeEnums}
     * 0=电子合同
     * 1=上传合同
     */
    private Integer contractSourceType;

    @Schema(description = "完成交易生成合同的交易平台的代码/合同来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String platform;

    public Boolean isAddOrderContractParamFieldDO() {
        // 将字段放入数组
        Object[] fields = {acceptanceType, performMainBody, isInviteSupplier, isInviteExpert, isInviteServiceObject, isProfessionalDetection,
                isSpotCheck, spotCheckProportion, isDestructiveCheck, destructiveCheckMethod, otherPreparations, acceptanceDate,
                contractAcpMethod, acceptanceWorkArrange, acceptanceContent, isSampleReference, acceptanceCriteria, acceptanceProcedure, otherContent};
        // 使用 Stream API 检查是否有任何字段非空
        boolean flag = Arrays.stream(fields).anyMatch(s -> ObjectUtil.isNotEmpty(s));
//        boolean anyFieldSet = Arrays.stream(fields).anyMatch(Optional::isPresent);
        if (flag) {
            return true;
        } else {
            return false;
        }
    }


}
