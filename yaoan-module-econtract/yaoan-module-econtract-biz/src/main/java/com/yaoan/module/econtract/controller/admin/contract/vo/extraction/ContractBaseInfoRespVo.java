package com.yaoan.module.econtract.controller.admin.contract.vo.extraction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 合同基本信息
 */
@Data
public class ContractBaseInfoRespVo {

    /**
     * 任务编号
     */
    @Schema(description = "任务编号")
    private String taskId;
    /**
     * 任务状态
     */
    @Schema(description = "任务状态")
    private String status;
    /**
     * 合同编号
     */
    @Schema(description = "合同编号")
    private String code;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称")
    private String name;

    /**
     * 合同签订日期
     */
    @Schema(description = "合同签订日期")
    private String contractSignTime;

    /**
     * 合同签订地点
     */
    @Schema(description = "合同签订地点")
    private String contractSignAddress;

    /**
     * 合同履约开始日期
     */
    @Schema(description = "合同履约开始日期")
    private String performStartDate;

    /**
     * 合同履约结束日期
     */
    @Schema(description = "合同履约结束日期")
    private String performEndDate;

    /**
     * 履约地点
     */
    @Schema(description = "履约地点")
    private String performAddress;

    /**
     * 合同金额
     */
    @Schema(description = "合同金额")
    private BigDecimal totalMoney;

    /**
     * 合同金额大写
     */
    @Schema(description = "合同金额大写")
    private String totalMoneyShift;

    /**
     * 是否缴纳履约保证金
     * 1：是 0：否
     */
    @Schema(description = "是否缴纳履约保证金 1：是 0：否")
    private Integer isPerformanceMoney;

    /**
     * 履约保证金金额
     */
    @Schema(description = "履约保证金金额")
    private BigDecimal performanceMoney;

    /**
     * 履约保证金缴纳方式
     */
    @Schema(description = "履约保证金缴纳方式")
    private String performanceMoneyType;
}
