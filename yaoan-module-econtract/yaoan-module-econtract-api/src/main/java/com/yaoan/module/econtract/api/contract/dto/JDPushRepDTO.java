package com.yaoan.module.econtract.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 合同信息对象
 *
 * @author zhc
 * @since 2023-12-05
 */
@Data
public class JDPushRepDTO {
    /**
     * 系统编码
     */
    @Schema(description = "系统编码", requiredMode = Schema.RequiredMode.REQUIRED)
    String systemCode;
    /**
     * 鉴权token
     */
    @Schema(description = "鉴权token", requiredMode = Schema.RequiredMode.REQUIRED)
    String token;
    /**
     * 鉴权KEY
     */
    @Schema(description = "鉴权KEY", requiredMode = Schema.RequiredMode.REQUIRED)
    String secretKey;
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    String contractGuid;
    /**
     * 订单ID
     */
    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    String orderGuid;
    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    String contractCode;
    /**
     * 合同备案结果
     * 1:已备案
     * 0:未备案
     */
    @Schema(description = "合同备案结果", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    Integer contractFillResult;

    /**
     * 合同备案时间
     */
    @Schema(description = "合同备案时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    Date contractFillTime;

    /**
     * 合同状态
     * 0	草稿箱
     * 1	合同已发送
     * 2	采购人确认
     * 3	供应商已盖章
     * 4	采购人已盖章
     */
    @Schema(description = "合同状态 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    Integer status;
    /**
     * 合同签署时间
     */
    @Schema(description = "合同签署时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    Date contractSignTime;
    /**
     * 采购人签章时间
     */
    @Schema(description = "采购人签章时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    Date purchaserSignTime;
    /**
     * 供应商签章时间
     */
    @Schema(description = "供应商签章时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    Date supplierSignTime;
    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String contractName;
    /**
     * 合同地址
     */
    @Schema(description = "合同地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String contractUrl;

}
