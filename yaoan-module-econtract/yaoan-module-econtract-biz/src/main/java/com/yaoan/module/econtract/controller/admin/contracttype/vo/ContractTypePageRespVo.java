package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/23 13:43
 */
@Data
@Schema(description = "合同类型列表响应VO")
@ToString(callSuper = true)
public class ContractTypePageRespVo {
    /**
     * 合同类型ID
     */
    @Schema(description = "合同类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String id;

    /**
     * 合同类型编号
     */
    @Schema(description = "合同类型编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String code;

    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String name;

    /**
     * 合同类型分类id
     */
    @Schema(description = "合同类型分类id", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer typeCategory;

    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String categoryName;

    /**
     * 创建者
     */
    @Schema(description = "创建者", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String creator;

    /**
     * 创建者昵称
     */
    @Schema(description = "创建者昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String creatorName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
