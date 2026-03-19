package com.yaoan.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * @author doujiale
 */
@Data
@Schema(description = "app登录参数，可以使用clientId+code登录，也可以使用openId登录")
@ToString(callSuper = true)
public class AuthAppClientReqVO {

    @Schema(description = "客户端获取的临时凭证(仅一次有效)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @Schema(description = "用户身份标识", requiredMode = Schema.RequiredMode.REQUIRED)
    private String openId;
}
