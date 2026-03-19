package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * 履约验收要求信息
 */
@Data
@XmlRootElement(name = "AcceptanceInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class AcceptanceInformation {
    /**
     * 验收组织方式
     */
    private String acceptanceType;

    /**
     * 验收主体
     */
    private String performMainBody;

    /**
     * 是否邀请本项目其他供应商
     */
    private Boolean isInviteSupplier;

    /**
     * 是否邀请验收评审专家
     */
    private Boolean isInviteExpert;

    /**
     * 是否邀请服务对象
     */
    private Boolean isInviteServiceObject;

    /**
     * 是否邀请第三方检测机构
     */
    private Boolean isProfessionalDetection;

    /**
     * 是否进行抽查检测(默认:0)
     */
    private Boolean isSpotCheck;

    /**
     * 抽查检测比例
     */
    private Double spotCheckProportion;

    /**
     * 是否存在破坏性检测(默认:0)
     */
    private Boolean isDestructiveCheck;

    /**
     * 被破坏的检测产品的处理方式
     */
    private String destructiveCheckMethod;

    /**
     * 组织验收的其他事项
     */
    private String otherPreparations;

    /**
     * 预计自供应商提出之日起N日内进行验收
     */
    private Integer infewdays;

    /**
     * 履约验收时间
     */
    private Date acceptanceDate;

    /**
     * 验收方法
     */
    private String contractAcpMethod;

    /**
     * 分期/分项验收工作安排
     */
    private String acceptanceWorkArrange;

    /**
     * 验收内容
     */
    private String acceptanceContent;

    /**
     * 是否以采购活动中供应商提供的样品作为参考
     */
    private Boolean isSampleReference;

    /**
     * 验收标准
     */
    private String acceptanceCriteria;

    /**
     * 履约验收程序
     */
    private String acceptanceProcedure;

    /**
     * 履约验收其他事项
     */
    private String otherContent;
}

