package com.yaoan.module.system.controller.admin.user.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 用户OPENID绑定 Request VO")
@Data
public class UserOpenIdReqVO{

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户编号不能为空")
    private Long id;

    @Schema(description = "用户身份标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户身份标识不能为空")
    private String userOpenId;
}
