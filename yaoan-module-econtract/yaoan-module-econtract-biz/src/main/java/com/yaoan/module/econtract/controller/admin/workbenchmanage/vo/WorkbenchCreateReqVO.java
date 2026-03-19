package com.yaoan.module.econtract.controller.admin.workbenchmanage.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 工作台管理创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkbenchCreateReqVO extends WorkbenchBaseVO {

}
