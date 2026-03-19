package com.yaoan.module.system.controller.admin.org.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * RegionReqVO
 */
@Schema(description = "管理后台 - 系统对接用户关系分页 Request VO")
@Data
@ToString(callSuper = true)
public class OrgReqVo  extends PageParam {

    @Schema(description = "单位名称")
    private String name;

    @Schema(description = "是否开通")
    private Integer isOpen;

    // 已开通的单位ids
    private List<String> orgIds;
}
