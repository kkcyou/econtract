package com.yaoan.module.system.controller.admin.region.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 区划精简信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionSimpleRespVO {

    @Schema(description = "区划编号")
    private String regionGuid;

    @Schema(description = "区划父ID", example = "1024")
    private String regionParentGuid;

    @Schema(description = "区划名称", example = "1024")
    private String regionName;

    @Schema(description = "区划编码")
    private String regionCode;

}
