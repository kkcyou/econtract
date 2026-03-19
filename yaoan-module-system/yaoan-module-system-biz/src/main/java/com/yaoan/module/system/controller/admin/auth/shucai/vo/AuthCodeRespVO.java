package com.yaoan.module.system.controller.admin.auth.shucai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 鉴权 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthCodeRespVO {

    @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED, example = "129")
    private String token_type;

    @Schema(description = "访问令牌", requiredMode = Schema.RequiredMode.REQUIRED, example = "happy")
    private String access_token;

    @Schema(description = "刷新令牌", requiredMode = Schema.RequiredMode.REQUIRED, example = "nice")
    private String refresh_token;

    @Schema(description = "过期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String expires_in;

    @Schema(description = "权限", requiredMode = Schema.RequiredMode.REQUIRED, example = "read write")
    private String scope;

    @Schema(description = "jti", requiredMode = Schema.RequiredMode.REQUIRED)
    private String jti;

}
