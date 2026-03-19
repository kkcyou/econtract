package com.yaoan.module.econtract.controller.admin.sign.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author doujiale
 */
@Schema(description = "合同文件 Request VO")
@Data
public class VerificationReqVO {

    @Schema(description = "合同文件", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同文件不能为空")
    private MultipartFile file;

}
