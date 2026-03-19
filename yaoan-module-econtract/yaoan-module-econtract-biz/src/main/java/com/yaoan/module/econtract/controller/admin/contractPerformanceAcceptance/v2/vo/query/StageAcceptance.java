package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.query;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/24 16:26
 */
@Data
public class StageAcceptance {
    /**
     * 验收时间
     */
    private Date acceptanceDate;

    /**
     * 预计结款时间
     */
    private Date expectedPayDate;

    /**
     * 验收申请
     * */
    private List<AcceptancePlanRespVO> acceptancePlanRespVOList;


}
