package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
public class ContractBaseVO implements Serializable {
    private static final long serialVersionUID = -7573553619762737705L;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "合同编码不能为空")
    @Size(max = 100, message = "合同编码最多100字")
    private String code;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同名称不能为空")
    private String name;

    /**
     * 签署截止日期
     */
    @Schema(description = "签署截止日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date expirationDate;

    /**
     * 合同分类
     */
    @Schema(description = "合同分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractCategory;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractType;

    /**
     * 合同描述
     */
    @Schema(description = "合同描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractDescription;

    /**
     * 签署文件名称
     */
    @Schema(description = "签署文件名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;

    /**
     * 关联的模板id
     */
    @Schema(description = "关联的模板id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String templateId;
}
