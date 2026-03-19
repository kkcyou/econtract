package com.yaoan.module.econtract.api.relative.dto;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-28 15:24
 */

import com.yaoan.module.econtract.enums.saas.RealNameEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RelativeCompanyDTO {
    /** 公司id */
    private Long companyId;

    /** 相对方id */
    private String relativeId;
    /**
     * {@link RealNameEnums}
     * */
    @Schema(description = "实名情况", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer realName;
}
