package com.yaoan.module.system.controller.admin.systemuserrel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 系统对接用户关系创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SystemuserRelCreateReqVO extends SystemuserRelBaseVO {

}
