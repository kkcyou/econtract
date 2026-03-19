package com.yaoan.module.econtract.controller.admin.warning.vo;

import com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo.PatrolContractInfoVO;
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
public class ContractBakDetailRespVO implements Serializable {

    private static final long serialVersionUID = -8598607678213427263L;
    /**
     * 合同基本信息
     */
    @Schema(description = "合同基本信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private PatrolContractInfoVO contractInfoVO;
    /**
     * 合同预警信息
     */
    @Schema(description = "合同预警信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ContractBakEarlyWarningVO contractBakEarlyWarningVO;
    /**
     * 合同项目信息
     */
    @Schema(description = "合同项目信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ContractBakProjectVO contractBakProjectVO;


}
