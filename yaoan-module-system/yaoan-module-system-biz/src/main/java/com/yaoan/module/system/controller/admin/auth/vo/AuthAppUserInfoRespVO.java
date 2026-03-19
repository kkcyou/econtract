package com.yaoan.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author doujiale
 */
@Schema(description = "管理后台 - 登录 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthAppUserInfoRespVO {

    @Schema(description = "用户身份标识", requiredMode = Schema.RequiredMode.REQUIRED)
    private String openId;
    @Schema(description = "身份证号(仅存在user授权时返回)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cardNum;
    @Schema(description = "真实姓名(仅存在user授权时返回)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String realName;
    @Schema(description = "用户手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userTel;
}
