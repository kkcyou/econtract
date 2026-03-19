package com.yaoan.module.econtract.controller.admin.businessroleformfield.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;


@Schema(description = "管理后台 - 获取角色字段关系 Request VO")
@Data
@ToString(callSuper = true)
public class BusinessRoleFormFieldReqVO  {

    @Schema(description = "角色id", example = "17963")
    private String roleId;

    @Schema(description = "编码")
    private String fieldCode;

    @Schema(description = "名称", example = "赵六")
    private String fieldName;

    @Schema(description = "所属字段id", example = "4322")
    private String fieldId;

    @Schema(description = "所属表单id", example = "22904")
    private String formId;

    @Schema(description = "所属业务id", example = "28787")
    private String businessId;

    @Schema(description = "是否展示")
    private Integer isShow;

   

}