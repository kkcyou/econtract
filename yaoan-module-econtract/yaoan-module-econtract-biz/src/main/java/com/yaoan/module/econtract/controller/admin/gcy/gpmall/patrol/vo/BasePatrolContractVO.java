package com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 巡检合同列表请求参数vo
 *
 * @author zhc
 * @since 2024-04-02
 */
@Data
public class BasePatrolContractVO implements Serializable {

    private static final long serialVersionUID = -1349931962338869625L;

    /**
     * 合同类型：项目采购、框架协议采购、电子卖场,无过程采购
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> contractTypes;

    /**
     * 采购人名称
     */
    @Schema(description = "采购人(采购单位)名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerOrgName;

    /**
     * 区划编号
     */
    @Schema(description = "区划编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionCode;
    /**
     * 预警类型 0：超期未签订，1：超期已签订
     */
    @Schema(description = "预警类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer overType;

    /**
     * 巡检类型 0：签订时效性，1：备案时效性
     */
    @Schema(description = "巡检类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotNull(message = "巡检类型不能为空")
    private Integer type;
}
