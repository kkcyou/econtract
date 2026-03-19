package com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 对外合同 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class OutwardContractBaseVO {

    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同编码不能为空")
    private String code;

    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotNull(message = "合同名称不能为空")
    private String name;

    @Schema(description = "解除之前的状态", example = "2")
    private Integer oldStatus;

    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "合同内容")
    private byte[] contractContent;

    @Schema(description = "合同分类")
    private Integer contractCategory;

    @Schema(description = "合同类型", example = "1")
    private String contractType;

    @Schema(description = "合同描述", example = "你说的对")
    private String contractDescription;

    @Schema(description = "签署文件名称", example = "王五")
    private String fileName;

    @Schema(description = "文件地址id", requiredMode = Schema.RequiredMode.REQUIRED, example = "28572")
    private Long fileAddId;

    @Schema(description = "文件pdf地址id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6274")
    private Long pdfFileId;

    private String userId;

    @Schema(description = "部门标识", example = "24868")
    private Long deptId;

    @Schema(description = "公司id", example = "18786")
    private Long companyId;

}
