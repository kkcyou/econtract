package com.yaoan.module.system.controller.admin.econtractorg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 电子合同单位信息更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EcontractOrgSaveReqVO extends EcontractOrgBaseVO {

    @Schema(description = "单位id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12231")
    private String id;

}
