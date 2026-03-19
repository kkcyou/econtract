package com.yaoan.module.econtract.controller.admin.supervise.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 合同分页列表返回数据
   */

@Data
public class GPXContractPageVO {
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;
    /**
     * 分类名称
     */
    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String categoryName;
    /**
     * 采购分类编码
     */
    @Schema(description = "采购分类编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purCatalogType;
    /**
     * 采购分类名称
     */
    @Schema(description = "采购分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purCatalogTypeName;
    /**
     * 合同编码
       */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    /**
     * 合同名称
       */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    /**
     * 合同总金额
     */
    @Schema(description = "合同总金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double totalMoney;
    /**
     * 采购人名称
     */
    @Schema(description = "采购人名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerLegalPerson;
    /**
     * 采购单位名称
     */
    @Schema(description = "采购人名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgName;

    /**
     * 签约日期
     */
    @Schema(description = "签约日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date contractSignTime;
    private String contractSignTimeBack;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierName;
    /**
     * 合同来源编码
     */
    @Schema(description = "合同来源编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String platform;
    /**
     * 合同来源名称
     */
    @Schema(description = "合同来源名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String platformName;

    /**
     * 履约结束时间
       */
    @Schema(description = "履约结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date performEndDate;
    /**
     * 履约开始时间
       */
    @Schema(description = "履约开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date performStartDate;


    /**
     * 合同状态
       */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
    /**
     * 合同状态名称
       */
    @Schema(description = "合同状态名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String statusName;
    /**
     * 在此交易平台中包的唯一识别码
     */
    @Schema(description = "在此交易平台中包的唯一识别码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bidGuid;
    /**
     * 备案状态 ContractIsBakStatusEnums
     */
    @Schema(description = "备案状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isBak;

    /**
     * 任务id
     */
    @Schema(description = "任务id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String taskId;

    /**
     * 流程id
     */
    @Schema(description = "流程id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String processInstanceId;

    /**
     * 甲方指派联系人 -甲方代表- 采购人指派联系人 对应 value13   采购人代表
     */
    @Schema(description = "甲方指派联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerLink;



    @Schema(description = "代理机构", requiredMode = Schema.RequiredMode.REQUIRED)
    private String agencyName;

    @Schema(description = "包id", requiredMode = Schema.RequiredMode.REQUIRED)
     private String packageGuid;
    @Schema(description = "包名称", requiredMode = Schema.RequiredMode.REQUIRED)
     private String packageName;
    @Schema(description = "包号", requiredMode = Schema.RequiredMode.REQUIRED)
     private String packageNumber;
    @Schema(description = "包预算", requiredMode = Schema.RequiredMode.REQUIRED)
    private String amount;
    @Schema(description = "采购方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purchaseMethodCode;

    @Schema(description = "所在地", requiredMode = Schema.RequiredMode.REQUIRED)
    private String zoneName ;
    private String remark9;
    @Schema(description = "分类", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String category;
    /**
     * 关联的模板id-此处范本id
     */
    @Schema(description = "关联的模板id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String templateId;

    /**
     * 项目id，以此查询项目信息
     */
    @Schema(description = "项目id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectGuid;

    /**
     * 招标项目方式名称：
     * common:一般项目采购、
     * batch:批量集中采购、
     * union:联合采购、
     * other:其他
     */
    private String biddingMethodCode;

    /**
     * 项目类型（货物、服务、工程）
     */
    private String projectType;
    /**
     * 项目类型名称
     */
    private String projectTypeName;
}
