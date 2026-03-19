package com.yaoan.module.econtract.controller.admin.agreement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Schema(description = "Contract PageResp VO")
@Data
@EqualsAndHashCode()
public class PrefAgreementRelRespVO implements Serializable {
    private static final long serialVersionUID = 8901559246352312280L;

    /**
     * 文件地址id
     */
    @Schema(description = "文件地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long infraFileId;

    /**
     * 文件名称
     */
    @Schema(description = "文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;
}
