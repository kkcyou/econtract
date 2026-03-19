package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xhx
 */
@Data
public class GpxPrjAndGpMallNumVO {
    /**
     * 交易执行任务数量
     * gpxProjectNum
     *
     */
    @Schema(description = "交易执行任务数量")
    private Long gpxPrjNum;
    /**
     * 服务工程超市任务数量
     * zhubajieNum
     *
     */
    @Schema(description = "服务工程超市任务数量")
    private Long zhubajieNum;

    /**
     * 框架协议任务数量
     * agreementNum
     *
     */
    @Schema(description = "框架协议任务数量")
    private Long agreementNum;

    /**
     * 协议定点任务数量
     * gpMallNum
     *
     */
    @Schema(description = "协议定点任务数量")
    private Long gpMallNum;

    /**
     * 电子卖场任务数量
     * jdMallNum
     *
     */
    @Schema(description = "电子卖场任务数量")
    private Long jdMallNum;
}
