package com.yaoan.module.econtract.controller.admin.gpx.contractVO;

import com.yaoan.module.econtract.controller.admin.gpx.vo.GPXContractQuotationRelRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.PlanDetailInfoRespVO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/8/1 11:57
 */
@Data
public class GPXContractPlaybackV3RespVO extends  GPXContractPlaybackRespVO{
    /**
     * 报价明细（采购内容）
     */
    private List<GPXContractQuotationRelRespVO> quotationRelRespVOList;

    /**
     * 计划明细
     */
    private List<PlanDetailInfoRespVO> planDetailInfoRespVOList;

    /**
     * 1=单供应商，2=多供应商（非联合体），3=多供应商（联合体）
     * */
    private Integer supplierType;

    /**
     * 项目类型，货物，工程，服务
     */
    private String projectType;
}
