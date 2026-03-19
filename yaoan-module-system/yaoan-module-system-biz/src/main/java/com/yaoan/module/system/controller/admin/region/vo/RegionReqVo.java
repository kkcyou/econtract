package com.yaoan.module.system.controller.admin.region.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * RegionReqVO
 */
@Data
public class RegionReqVo {

    /**
     * 区划编码
     */
    private String regionCode;

    @Schema(description = "区划父ID", example = "1")
    private String regionParentGuid;
}
