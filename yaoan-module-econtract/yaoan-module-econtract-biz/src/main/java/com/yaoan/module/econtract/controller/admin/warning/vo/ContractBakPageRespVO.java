package com.yaoan.module.econtract.controller.admin.warning.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 备案时效性列表数据返回 vo
 * @author zhc
 */
@Data
public class ContractBakPageRespVO implements Serializable {

    private static final long serialVersionUID = -425310987587952853L;
    //合同名称、合同编号、采购单位、合同金额（元）、签订日期、超期天数、是否已发短信
    /**
     * 合同ID
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String contractId;
    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String contractName;
    /**
     * 合同编号
     */
    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String contractCode;
    /**
     * 采购单位名称
     */
    @Schema(description = "采购单位名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerOrgName;
    /**
     * 合同金额
     */
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal totalMoney;
    /**
     * 合同签章时间
     */
    @Schema(description = "签章时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date supSinTime;
    /**
     * 超期时间(天)
     */
    @Schema(description = "超期天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long OverDays;
    /**
     * 签署状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;
    /**
     * 备案时间
     */
    @Schema(description = "备案时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date bakDate;
    /**
     * 是否备案超期  0：未超期   1：已超期
     */
    @Schema(description = "是否备案超期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer bakFlag;
    /**
     * 采购人区划编码
     */
    @Schema(description = "采购人区划编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionCode;

    /**
     * 合同来源
     */
    private String platform;
    /**
     * 合同来源名称
     */
    private String platformName;

    /**
     * 预警级别
     */
    private String warningLevel;
}
