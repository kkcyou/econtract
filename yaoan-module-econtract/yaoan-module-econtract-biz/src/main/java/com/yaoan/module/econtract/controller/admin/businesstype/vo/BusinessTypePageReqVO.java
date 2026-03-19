package com.yaoan.module.econtract.controller.admin.businesstype.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Schema(description = "管理后台 - 业务类型分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BusinessTypePageReqVO extends PageParam {

    @Schema(description = "单据类型编号")
    private String code;

    @Schema(description = "单据类型名称", example = "赵六")
    private String name;

    @Schema(description = "数据表", example = "张三")
    private String tableName;

    

}