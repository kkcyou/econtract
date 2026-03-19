package com.yaoan.module.econtract.controller.admin.supervise.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 合同分页列表返回数据
   */

@Data
public class ContractPageRespVO {
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;
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
    @Schema(description = "采购人名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgName;
    /**
     * 签约日期
     */
    @Schema(description = "签约日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date contractSignTime;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SupplierVO> supplierNames;
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

}
