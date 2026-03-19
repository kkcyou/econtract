package com.yaoan.module.system.controller.admin.oauth2.vo.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* OAuth2 客户端 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 * @author doujiale
 */
@Data
public class OAuth2AccessCodeReqVO {

    @Schema(description = "客户端编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "tudou")
    @NotNull(message = "客户端编号不能为空")
    private String clientId;

    @Schema(description = "客户端密钥", requiredMode = Schema.RequiredMode.REQUIRED, example = "fan")
    @NotNull(message = "客户端密钥不能为空")
    private String secret;

}
