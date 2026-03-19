package com.yaoan.module.econtract.controller.admin.freezed.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "TerminateContract CreateReqVO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FreezedContractCreateReqVO extends FreezedContractBaseVO {
        private static final long serialVersionUID = -3080459683177272331L;

//        @Schema(description = "终止文件", requiredMode = Schema.RequiredMode.REQUIRED)
//        private MultipartFile file;
}
