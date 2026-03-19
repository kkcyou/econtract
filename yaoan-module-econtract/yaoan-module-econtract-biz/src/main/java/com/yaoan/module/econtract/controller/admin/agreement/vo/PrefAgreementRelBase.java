package com.yaoan.module.econtract.controller.admin.agreement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
@Data
public class PrefAgreementRelBase  implements Serializable {
    private static final long serialVersionUID = 3181829198552003428L;

    /**
     * 履约id
     */
    @Schema(description = "履约id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String prefId;

    /**
     * 文件地址id
     */
    @Schema(description = "文件地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long infraFileId;

    /**
     *文件名称
     */
    @Schema(description = "文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;
}
