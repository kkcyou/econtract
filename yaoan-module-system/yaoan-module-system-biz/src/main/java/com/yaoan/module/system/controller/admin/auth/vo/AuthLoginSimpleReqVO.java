package com.yaoan.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/30 13:37
 */
@Schema(description = "管理后台 - 账号密码登录 AuthLoginSimpleReqVO VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLoginSimpleReqVO {

    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "test222")
    @NotEmpty(message = "登录账号不能为空")
//    @Length(min = 4, max = 21, message = "账号长度为 4-21 位")
//    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "账号格式为数字以及字母")
    private String username;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "test222")
    @NotEmpty(message = "密码不能为空")
//    @Length(min = 8, max = 20, message = "密码长度为 8-20 位")
    private String password;

    @Schema(description = "邀请码", requiredMode = Schema.RequiredMode.REQUIRED, example = "test222")
//    @NotEmpty(message = "邀请码不能为空")
//    @Length(min = 8, max = 20, message = "密码长度为 8-20 位")
    private String inviteCode;

}
