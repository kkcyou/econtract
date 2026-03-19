package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 审查清单-审查规则关联新增/修改 Request VO")
@Data
public class RelChecklistRuleSaveReqVO {

    @Schema(description = "关联id")
    private String id;

    @Schema(description = "审查规则id", example = "1")
    private String reviewId;

    @Schema(description = "审查清单id", example = "946")
    private String reviewListId;

}