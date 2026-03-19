package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relcontracttypechecklist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 合同类型-审查清单关联新增/修改 Request VO")
@Data
public class RelContractTypeChecklistSaveReqVO {

    @Schema(description = "关联id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26560")
    private String id;

    @Schema(description = "合同类型编号", example = "2")
    private String contractType;

    @Schema(description = "审查清单id", example = "31057")
    private String reviewListId;

}