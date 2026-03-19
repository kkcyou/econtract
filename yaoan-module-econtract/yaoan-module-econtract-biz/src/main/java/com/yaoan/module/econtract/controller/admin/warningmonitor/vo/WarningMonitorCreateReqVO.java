package com.yaoan.module.econtract.controller.admin.warningmonitor.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 预警监控项配置表（new预警）创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarningMonitorCreateReqVO extends WarningMonitorBaseVO {

}
