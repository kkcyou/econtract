package com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 合同模板推荐 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ContractAiDraftShowBaseVO {

    @Schema(description = "模板名称", example = "赵六")
    private String templateName;

    @Schema(description = "使用的大模型")
    private String model;

    @Schema(description = "模板内容")
    private String templateContent;

    @Schema(description = "合同名称", example = "赵六")
    private String contractName;

    @Schema(description = "使用次数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer useNum;

    @Schema(description = "文件id")
    private String fileId;

}
