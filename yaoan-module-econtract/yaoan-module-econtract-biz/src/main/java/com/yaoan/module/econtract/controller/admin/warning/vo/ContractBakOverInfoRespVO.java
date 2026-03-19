package com.yaoan.module.econtract.controller.admin.warning.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 备案超期单位返回信息 vo
 * @author doujiale
 */
@Data
public class ContractBakOverInfoRespVO implements Serializable {

    private static final long serialVersionUID = 6202535705427474302L;

    /**
     * 排名序号
     */
    @Schema(description = "排名序号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer sort;
    /**
     * 超期总数量
     */
    @Schema(description = "超期总数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer overBakTotalNum=0;
    /**
     * 超期已备案数量
     */
    @Schema(description = "超期已备案数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer overOkBakNum=0;
    /**
     * 超期未备案数量
     */
    @Schema(description = "超期未备案数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer overNoBakNum=0;

    /**
     * 采购人名称
     */
    @Schema(description = "采购人(采购单位)名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgName;

    /**
     * 采购人id
     */
    @Schema(description = "采购人id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgId;


}
