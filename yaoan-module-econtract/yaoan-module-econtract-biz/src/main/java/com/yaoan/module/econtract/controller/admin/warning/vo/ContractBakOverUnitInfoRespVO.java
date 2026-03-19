package com.yaoan.module.econtract.controller.admin.warning.vo;

import com.yaoan.framework.common.pojo.PageResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 辅助计算DTO
 * @author doujiale
 */
@Data
public class ContractBakOverUnitInfoRespVO implements Serializable {

    private static final long serialVersionUID = 6202535705427474302L;

    /**
     * 采购单位数量
     */
    @Schema(description = "采购单位数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer buyerNum;

    /**
     * 超期已备案数量
     */
    @Schema(description = "超期已备案总数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer overOkBakTotalNum;
    /**
     * 超期未备案数量
     */
    @Schema(description = "超期未备案总数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer overNoBakTotakNum;
    /**
     * 备案超期单位返回信息列表
     */
    @Schema(description = "备案超期单位返回信息列表", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private PageResult<ContractBakOverInfoRespVO> contractBakOverInfoRespVOS;
}
