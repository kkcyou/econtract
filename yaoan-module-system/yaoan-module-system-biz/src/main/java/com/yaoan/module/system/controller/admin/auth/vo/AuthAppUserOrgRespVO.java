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
public class AuthAppUserOrgRespVO {

    @Schema(description = "组织机构名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orgName;

    @Schema(description = "组织机构编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creditCode;
}
