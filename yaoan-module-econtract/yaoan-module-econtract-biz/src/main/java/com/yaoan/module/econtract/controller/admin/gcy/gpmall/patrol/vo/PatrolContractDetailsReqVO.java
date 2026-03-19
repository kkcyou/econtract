package com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 巡检合同列表请求参数vo
 *
 * @author zhc
 * @since 2024-04-02
 */
@Data
public class PatrolContractDetailsReqVO implements Serializable {

    private static final long serialVersionUID = -1349931962338869625L;
    /**
     * 合同类型：项目采购、框架协议采购、电子卖场,无过程采购
     */
    @NotBlank
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractType;
    /**
     * 项目名称
     */
    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectName;

    /**
     * 项目编号
     */
    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectCode;
    /**
     * 订单编号
     */
    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderCode;
    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractCode;
    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractName;

}
