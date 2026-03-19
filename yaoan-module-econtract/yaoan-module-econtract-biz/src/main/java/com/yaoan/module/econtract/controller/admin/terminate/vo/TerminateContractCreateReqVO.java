package com.yaoan.module.econtract.controller.admin.terminate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "TerminateContract CreateReqVO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TerminateContractCreateReqVO extends TerminateContractBaseVO{
        private static final long serialVersionUID = -3080459683177272331L;

//        @Schema(description = "终止文件", requiredMode = Schema.RequiredMode.REQUIRED)
//        private MultipartFile file;
}
