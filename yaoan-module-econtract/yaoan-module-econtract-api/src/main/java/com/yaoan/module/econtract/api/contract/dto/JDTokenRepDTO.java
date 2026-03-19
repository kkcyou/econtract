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
public class JDTokenRepDTO {
    /**
     * 鉴权token
     */
    @Schema(description = "鉴权token", requiredMode = Schema.RequiredMode.REQUIRED)
    String accessKey;
    /**
     * 鉴权KEY
     */
    @Schema(description = "鉴权KEY", requiredMode = Schema.RequiredMode.REQUIRED)
    String secretKey;
    /**
     * 系统编码
     */
    @Schema(description = "系统编码", requiredMode = Schema.RequiredMode.REQUIRED)
    String systemCode;


}
