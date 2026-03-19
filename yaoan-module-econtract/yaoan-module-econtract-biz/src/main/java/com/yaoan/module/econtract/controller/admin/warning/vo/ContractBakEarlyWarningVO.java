package com.yaoan.module.econtract.controller.admin.warning.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 合同备案预警信息 VO
 *
 * @author zhc
 * @date 2023/7/24
 */
@Data
public class ContractBakEarlyWarningVO {
    /**
     * 签订时间
     */
    @Schema(description = "签订时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date sinTime;
    /**
     * 超期时间(天)
     */
    @Schema(description = "超期天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long OverDays;
    /**
     * 签订截止时间
     */
    @Schema(description = "备案截止时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date contractBakEndTime;
    /**
     * 是否备案  0:未备案   1：已备案
     */
    @Schema(description = "是否备案", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isBak;

    /**
     * 预警级别
     */
    private String warningLevel;

    /**
     * 备案日期
     */
    @Schema(description = "备案时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date bakDate;
}
