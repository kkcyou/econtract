package com.yaoan.module.econtract.controller.admin.outward.contract.VO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class ApiStatusReqVO implements Serializable {
    private static final long serialVersionUID = 6174826638225621263L;
    @Schema(description = "主键", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    /**
     * 签署状态
     * 0-待发送 - 新增
     * 1-被退回
     * 2-已发送
     * 3-待确认
     * 4-待签署
     * 5-已拒签
     * 6-签署完成
     * 7-逾期未签署
     * 8-合同终止签署中
     * 9-合同终止
     * 10-合同变更
     * 11-待送审
     * 12-审核中
     * 13审核未通过
     */
    @Schema(description = "签署状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;
}
