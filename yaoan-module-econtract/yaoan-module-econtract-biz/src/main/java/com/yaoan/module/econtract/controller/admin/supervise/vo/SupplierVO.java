package com.yaoan.module.econtract.controller.admin.supervise.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 服务实现类
 *
 * @author ZHC
 * @since 2024-03-18
 */
@Data
public class SupplierVO {
    /**
     * 合同供应商唯一识别码
     */
    @Schema(description = "合同供应商唯一识别码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "合同供应商唯一识别码不能为空")
    private String	supplierGuid;

    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商名称不能为空")
    private String	supplierName;
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractId;
    /**
     * 供应商社会信用代码/组织机构代码/个人身份证/供应商的纳税人识别号
     */
    @Schema(description = "供应商社会信用代码/组织机构代码/个人身份证", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商社会信用代码/组织机构代码/个人身份证不能为空")
    private String	supplierCode;
    /**
     * 供应商负责人 对应 value14
     */
    @Schema(description = "供应商联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierProxy;
    /**
     * 供应商负责人地址 对应 value19
     */
    @Schema(description = "供应商联系人地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String registeredAddress;

}
