package com.yaoan.module.system.controller.admin.user.vo.user;

import com.yaoan.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 用户更新密码 Request VO")
@Data
public class UserForgetPasswordReqVO {

    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String accountName;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotEmpty(message = "密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String password;

    @Schema(description = "短信验证码")
    private String smsCode;
    @Mobile
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "手机号不能为空")
    private String mobile;
}
