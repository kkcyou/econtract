package com.yaoan.module.system.controller.admin.auth.shucai.vo;

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
@Schema(description = "管理后台 - code token AuthTokenCodeReqVO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthTokenCodeReqVO {


    @Schema(description = "授权码", requiredMode = Schema.RequiredMode.REQUIRED, example = "BJYA")
    @NotEmpty(message = "授权码不能为空")
    private String code;

    @Schema(description = "客户端的当前状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "BJYA")
    private String state;

    @Schema(description = "重定向地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "http://localhost/login?target=workbench")
    private String redirectUri;

}
