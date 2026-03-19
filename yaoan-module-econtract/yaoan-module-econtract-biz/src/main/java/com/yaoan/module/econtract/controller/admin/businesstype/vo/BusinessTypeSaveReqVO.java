package com.yaoan.module.econtract.controller.admin.businesstype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 业务类型新增/修改 Request VO")
@Data
public class BusinessTypeSaveReqVO {

    @Schema(description = "单据类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1141")
    private String id;

    @Schema(description = "单据类型编号")
    private String code;

    @Schema(description = "单据类型名称", example = "赵六")
    private String name;

    @Schema(description = "数据表", example = "张三")
    private String tableName;

}