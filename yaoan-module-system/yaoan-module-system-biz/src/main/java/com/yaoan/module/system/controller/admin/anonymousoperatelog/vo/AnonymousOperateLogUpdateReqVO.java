package com.yaoan.module.system.controller.admin.anonymousoperatelog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 匿名用户操作日志记录更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AnonymousOperateLogUpdateReqVO extends AnonymousOperateLogBaseVO {

    @Schema(description = "日志主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "201")
    @NotNull(message = "日志主键不能为空")
    private Long id;

}
