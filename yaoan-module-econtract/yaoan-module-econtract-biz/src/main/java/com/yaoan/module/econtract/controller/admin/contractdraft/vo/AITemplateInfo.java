package com.yaoan.module.econtract.controller.admin.contractdraft.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 合同智能起草记录 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class AITemplateInfo {

    @Schema(description = "模板Id")
    private String id;
    @Schema(description = "模板名称")
    private String name;
    @Schema(description = "合同类型")
    private String contractType;
    @Schema(description = "合同类型名称")
    private String contractTypeName;
    @Schema(description = "模板doc文件id")
    private Long remoteFileId;
    @Schema(description = "模板pdf文件id")
    private Long rtfPdfFileId;

}
