package com.yaoan.module.system.controller.admin.oauth2.vo.token;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 访问令牌 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2AccessJWTRespVO {

    @Schema(description = "sessionId", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String sessionId;

    @Schema(description = "过期时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "tudou")
    private Long expiresIn;

    @Schema(description = "重定向地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "nice")
    private String redirect;

    @Schema(description = "操作权限(按钮级别)，暂时不用", requiredMode = Schema.RequiredMode.REQUIRED, example = "666")
    private List<String> permissions;

    @Schema(description = "操作权限", requiredMode = Schema.RequiredMode.REQUIRED, example = "666")
    private String action;

    @Schema(description = "模式", requiredMode = Schema.RequiredMode.REQUIRED, example = "666")
    private String model;

}
