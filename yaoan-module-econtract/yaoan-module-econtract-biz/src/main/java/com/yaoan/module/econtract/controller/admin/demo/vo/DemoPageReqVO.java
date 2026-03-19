package com.yaoan.module.econtract.controller.admin.demo.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "demo Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class DemoPageReqVO extends PageParam {

    @Schema(description = "demo名称，模糊匹配", example = "yudao")
    private String name;

}
