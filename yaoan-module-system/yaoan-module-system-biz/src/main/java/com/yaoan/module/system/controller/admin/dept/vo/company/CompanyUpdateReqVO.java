package com.yaoan.module.system.controller.admin.dept.vo.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 单位更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyUpdateReqVO extends CompanyBaseVO {

    @Schema(description = "单位编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "单位编号不能为空")
    private Long id;

    @Schema(description = "身份证号", example = "15601691000")
    private String idCard;

    @Schema(description = "昵称")
    private String nickname;
}
