package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.yaoan.module.econtract.enums.ModelTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 签署文件信息
 */
@Data
public class ContractInfoVO {

    /**
     * 签署文件名称
     */
    @Schema(description = "签署文件名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;

    /**
     *主文件地址id
     */
    @Schema(description = "主文件地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long fileAddId;

    /**
     * 合同文件pdf 地址id
     */
    @Schema(description = "合同文件pdf地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pdfFileId;

    /**
     * 关联的模板id
     */
    @Schema(description = "关联的模板id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String templateId;

    /**
     * 模板的类型
     * 关联{@link ModelTypeEnum }
     */
    private String type;


}
