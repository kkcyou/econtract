package com.yaoan.module.system.controller.admin.anonymousoperatelog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 匿名用户操作日志记录创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AnonymousOperateLogCreateReqVO extends AnonymousOperateLogBaseVO {

}
