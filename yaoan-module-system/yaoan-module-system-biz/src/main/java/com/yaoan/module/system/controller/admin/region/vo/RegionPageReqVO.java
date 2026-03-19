package com.yaoan.module.system.controller.admin.region.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author doujiale
 */
@Schema(description = "管理后台 - 区划分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class RegionPageReqVO extends PageParam {


    @Schema(description = "区划名称，模糊匹配", example = "芋道")
    private String name;

    @Schema(description = "区划父ID", example = "1")
    private String regionParentGuid;

}
