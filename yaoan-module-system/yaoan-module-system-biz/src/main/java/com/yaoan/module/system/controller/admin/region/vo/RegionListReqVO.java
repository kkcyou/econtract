package com.yaoan.module.system.controller.admin.region.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author doujiale
 */
@Schema(description = "管理后台 - 区划列表 Request VO")
@Data
public class RegionListReqVO {

    @Schema(description = "区划名称，模糊匹配", example = "芋道")
    private String name;
    private String regionRoot;
}
