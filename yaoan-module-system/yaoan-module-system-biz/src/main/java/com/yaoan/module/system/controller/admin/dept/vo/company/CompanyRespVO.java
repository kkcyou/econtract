package com.yaoan.module.system.controller.admin.dept.vo.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 单位信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyRespVO extends CompanyBaseVO {

    @Schema(description = "单位编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "状态，参见 CommonStatusEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime createTime;

    @Schema(description = "身份证号", example = "15601691000")
    private String idCard;

    @Schema(description = "昵称")
    private String nickname;
}
