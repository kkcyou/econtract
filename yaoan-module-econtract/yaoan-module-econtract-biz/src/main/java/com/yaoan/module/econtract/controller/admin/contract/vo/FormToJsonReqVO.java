package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "智能填写-表单提取信息入参")
@Data
public class FormToJsonReqVO {
    /**
     * 文件id
     */
    @Schema(description = "文件id")
    Long fileId;

    /**
     * 表单id集合
     */
    @Schema(description = "表单id集合")
    List<String> formIdList;
}
