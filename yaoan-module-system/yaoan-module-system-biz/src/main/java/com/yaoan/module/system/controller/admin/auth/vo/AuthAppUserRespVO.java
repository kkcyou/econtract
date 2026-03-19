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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthAppUserRespVO {

    @Schema(description = "clientId(获取code的应用的clientId)", requiredMode = Schema.RequiredMode.REQUIRED)
    private AuthAppUserOrgRespVO org;

    @Schema(description = "客户端获取的临时凭证(仅一次有效)", requiredMode = Schema.RequiredMode.REQUIRED)
    private AuthAppUserInfoRespVO user;
}
