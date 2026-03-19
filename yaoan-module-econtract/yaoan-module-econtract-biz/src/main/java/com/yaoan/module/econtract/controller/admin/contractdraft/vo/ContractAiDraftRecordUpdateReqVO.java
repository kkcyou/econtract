package com.yaoan.module.econtract.controller.admin.contractdraft.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 合同智能起草记录更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractAiDraftRecordUpdateReqVO extends ContractAiDraftRecordBaseVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1529")
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
