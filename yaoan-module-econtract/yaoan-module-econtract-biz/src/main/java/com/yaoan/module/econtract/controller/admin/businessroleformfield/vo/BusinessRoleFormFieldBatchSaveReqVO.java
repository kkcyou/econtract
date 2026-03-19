package com.yaoan.module.econtract.controller.admin.businessroleformfield.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "管理后台 - 角色字段关系新增/修改 Request VO")
@Data
public class BusinessRoleFormFieldBatchSaveReqVO {
    @Schema(description = "角色id", requiredMode = Schema.RequiredMode.REQUIRED, example = "17963")
    @NotEmpty(message = "角色id不能为空")
    private String roleId;

    @Schema(description = "所属表单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22904")
    @NotEmpty(message = "所属表单id不能为空")
    private String formId;

    @Schema(description = "所属业务id", requiredMode = Schema.RequiredMode.REQUIRED, example = "28787")
    @NotEmpty(message = "所属业务id不能为空")
    private String businessId;
    
    List<BusinessRoleFormFieldSaveReqVO> fieldList;

}