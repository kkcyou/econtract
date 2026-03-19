package com.yaoan.module.econtract.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


/**
 * @description: 查询合同状态返回参数
 * @author: wsh
 * @date: 2024-04-25
 */
@Data
public class PushContractDataDTO implements Serializable {
    private static final long serialVersionUID = 6056488516698227018L;
    /**
     * 合同ID
     */
    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractGuid;
    /**
     * 订单ID
     */
    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderGuid;
    /**
     * 当前合同的状态
     */
    @Schema(description = "当前合同的状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;
    /**
     * 合同状态名称
     */
    @Schema(description = "合同状态名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String statusName;
    /**
     * 状态变更的操作时间
     */
    @Schema(description = "状态变更的操作时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long operateTime;
    /**
     * 备注信息
     */
    @Schema(description = "备注信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;




}
