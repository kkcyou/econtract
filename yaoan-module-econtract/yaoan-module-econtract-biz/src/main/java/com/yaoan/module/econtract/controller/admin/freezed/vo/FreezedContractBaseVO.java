package com.yaoan.module.econtract.controller.admin.freezed.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class FreezedContractBaseVO implements Serializable {

    private static final long serialVersionUID = 5951449313902131516L;

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    /**
     * 主合同id
     */
    @Schema(description = "主合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "主合同id不能为空")
    private String contractId;

    /**
     * 签署截止日期
     */
    @Schema(description = "签署截止日期", requiredMode = Schema.RequiredMode.REQUIRED)
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone="GMT+8",pattern = "yyyy-MM-dd")
    private Date expirationDate;

    /**
     * 文件地址id
     */
    @Schema(description = "文件地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long fileAddId;

    /**
     * 文件名称
     */
    @Schema(description = "文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;
}
