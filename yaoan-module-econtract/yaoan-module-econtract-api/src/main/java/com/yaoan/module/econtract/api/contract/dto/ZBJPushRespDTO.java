package com.yaoan.module.econtract.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 合同信息对象
 *
 * @author zhc
 * @since 2023-12-05
 */
@Data
public class ZBJPushRespDTO {
    /**
     * 平台值，服务⼯程超市分配，固定值为7
     */
    @Schema(description = "平台值", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer platform;
    /**
     * 当前请求ID，唯⼀标识
     */
    @Schema(description = "当前请求ID", requiredMode = Schema.RequiredMode.REQUIRED)
    String requestId;
    /**
     * 当前请求时间戳，毫秒数
     */
    @Schema(description = "当前请求时间戳，毫秒数", requiredMode = Schema.RequiredMode.REQUIRED)
    Long timestamp;
    /**
     * 签名
     */
    @Schema(description = "签名", requiredMode = Schema.RequiredMode.REQUIRED)
    String sign;
    /**
     * 业务参数
     */
    @Schema(description = "业务参数", requiredMode = Schema.RequiredMode.REQUIRED)
    PushContractDataDTO data;


}
