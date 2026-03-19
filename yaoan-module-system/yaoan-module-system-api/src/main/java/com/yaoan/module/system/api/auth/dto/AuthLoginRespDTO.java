package com.yaoan.module.system.api.auth.dto;

import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-30 21:12
 */
@Data
public class AuthLoginRespDTO {
    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "129")
    private Long userId;

    @Schema(description = "访问令牌", requiredMode = Schema.RequiredMode.REQUIRED, example = "happy")
    private String accessToken;

    @Schema(description = "刷新令牌", requiredMode = Schema.RequiredMode.REQUIRED, example = "nice")
    private String refreshToken;

    @Schema(description = "过期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime expiresTime;

    @Schema(description = "首次登陆", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer firstLogin;

    @Schema(description = "租户标识")
    private Long tenantId;
    private String businessKey;

    private AdminUserRespDTO adminUserRespDTO;
}
