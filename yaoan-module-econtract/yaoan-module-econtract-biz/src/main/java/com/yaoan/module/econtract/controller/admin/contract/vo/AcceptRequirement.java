package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

/**
 * 合同履约验收要求
 */
@Data
public class AcceptRequirement {
    /**
     * 验收组织方式
     */
    private String organizeMethod;

    /**
     * 验收主体
     */
    private String acceptSubject;

    /**
     * 是否邀请本项目其他供应商
     * （1：是 0：否）
     */
    private Integer isInviteOtherSupplier;

    /**
     * 是否邀请验收评审专家
     * （1：是 0：否）
     */
    private Integer isInviteProfessional;

    /**
     * 是否邀请服务对象
     * （1：是 0：否）
     */
    private Integer isInviteServiceTarget;

    /**
     * 是否邀请第三方检测机构
     * （1：是 0：否）
     */
    private Integer isInviteTestingAgency;

    /**
     * 是否进行抽查检测
     * （1：是 0：否）
     */
    private Integer isSpotCheck;

    /**
     * 抽查检测比例
     */
    private Number spotCheckRatio;

    /**
     * 是否存在破坏性检测
     * （1：是 0：否）
     */
    private Integer isDestroyCheck;

    /**
     * 被破坏的检测产品的处理方式
     */
    private String destroyProductDisposal;

    /**
     * 组织验收的其他事项
     */
    private String otherAcceptItem;

    /**
     * 履行验收时间
     */
    private String acceptDateDesc;

    /**
     * 验收方法
     */
    private String acceptMethod;

    /**
     * 履行验收程序
     */
    private String acceptProcedure;

    /**
     * 验收内容（技术）
     */
    private String acceptContentTech;

    /**
     * 验收内容（商务）
     */
    private String acceptContentCommerce;

    /**
     * 验收标准
     */
    private String acceptStandard;

    /**
     * 验收计划其他内容
     */
    private String acceptOtherPlan;


 }
