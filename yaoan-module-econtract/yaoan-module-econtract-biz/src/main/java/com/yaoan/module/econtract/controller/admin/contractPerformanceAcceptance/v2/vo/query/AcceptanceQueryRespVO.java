package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.query;

import com.yaoan.module.econtract.controller.admin.wps.FileVO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/22 14:05
 */
@Data
public class AcceptanceQueryRespVO {

    /**
     * 申请信息
     */
    private ApplyInfoRespVO applyInfoRespVO;

    /**
     * 合同信息
     */
    private AcceptanceContractRespVO contractRespVO;

    /**
     * 验收申请
     */
    private StageAcceptance stageAcceptance;


    /**
     * 验收材料
     */
    private List<FileVO> fileVOList;

    private String remark;

}
