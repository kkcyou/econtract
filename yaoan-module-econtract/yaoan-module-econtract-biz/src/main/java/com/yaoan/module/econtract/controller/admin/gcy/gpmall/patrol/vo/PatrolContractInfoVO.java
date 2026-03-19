package com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 巡检合同列表请求参数vo
 *
 * @author zhc
 * @since 2024-04-02
 */
@Data
public class PatrolContractInfoVO implements Serializable {

    private static final long serialVersionUID = 1857071804237306228L;
    /**
     * 合同ID
     */
    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;
    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractCode;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractName;
    /**
     * 采购人名称
     */
    @Schema(description = "采购单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgName;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierName;
    /**
     * 合同金额
     */
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal totalMoney;
    /**
     * 合同URL
     */
    @Schema(description = "合同URL", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String pdfFilePath;
    /**
     * 合同文件id
     */
    @Schema(description = "合同文件id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pdfFileId;

    /**
     * 合同状态
     */
    private Integer status;
}
