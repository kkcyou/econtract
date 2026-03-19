package com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 巡检合同列表请求参数vo
 *
 * @author zhc
 * @since 2024-04-02
 */
@Data
public class PatrolContractDetailRespVO implements Serializable {

    private static final long serialVersionUID = 2664591285076564697L;
    /**
     * 合同基本信息
     */
    @Schema(description = "合同基本信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private PatrolContractInfoVO contractInfoVO;
    /**
     * 合同巡检信息
     */
    @Schema(description = "合同巡检信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private PatrolContractPageRespVO patrolContractPageRespVO;
    /**
     * 合同项目信息--电子交易有
     */
    @Schema(description = "合同项目信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private PatrolProjectInfoVO patrolProjectInfoVO;
    /**
     * 订单信息--电子卖场，框彩有
     */
    @Schema(description = "订单信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private PatrolOrderVO patrolOrderVO;


}
