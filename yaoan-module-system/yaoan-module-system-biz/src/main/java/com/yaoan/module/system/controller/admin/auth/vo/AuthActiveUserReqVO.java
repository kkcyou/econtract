package com.yaoan.module.system.controller.admin.auth.vo;
import cn.hutool.core.util.StrUtil;
import com.yaoan.framework.common.validation.InEnum;
import com.yaoan.framework.common.validation.Mobile;
import com.yaoan.module.system.enums.social.SocialTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author Pele
 */
@Schema(description = "管理后台 - 账号密码登录 Request VO，如果登录并绑定社交用户，需要传递 social 开头的参数")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthActiveUserReqVO {



    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "test222")
    @NotEmpty(message = "密码不能为空")
    @Length(min = 8, max = 20, message = "密码长度为 8-20 位")
    private String password;

    /**
     * 手机号
     */
    @Mobile
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15612345678")
    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    // ========== 图片验证码相关 ==========

    @Schema(description = "验证码，验证码开启时，需要传递", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "PfcH6mgr8tpXuMWFjvW6YVaqrswIuwmWI5dsVZSg7sGpWtDCUbHuDEXl3cFB1+VvCC/rAkSwK8Fad52FSuncVg==")
    @NotEmpty(message = "验证码不能为空")
    private String captchaVerification;

    // ========== 绑定社交登录时，需要传递如下参数 ==========

    @Schema(description = "社交平台的类型，参见 SysUserSocialTypeEnum 枚举值", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @InEnum(SocialTypeEnum.class)
    private Integer socialType;

    @Schema(description = "授权码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String socialCode;

    @Schema(description = "state", requiredMode = Schema.RequiredMode.REQUIRED, example = "9b2ffbc1-7425-4155-9894-9d5c08541d62")
    private String socialState;

    /**
     * 开启验证码的 Group
     */
    public interface CodeEnableGroup {}

    @AssertTrue(message = "授权码不能为空")
    public boolean isSocialCodeValid() {
        return socialType == null || StrUtil.isNotEmpty(socialCode);
    }

    @AssertTrue(message = "授权 state 不能为空")
    public boolean isSocialState() {
        return socialType == null || StrUtil.isNotEmpty(socialState);
    }

}
