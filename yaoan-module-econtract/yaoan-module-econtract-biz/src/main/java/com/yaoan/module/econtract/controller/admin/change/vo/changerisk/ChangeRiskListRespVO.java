package com.yaoan.module.econtract.controller.admin.change.vo.changerisk;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025-6-16 15:47
 */
@Data
public class ChangeRiskListRespVO {

    private List<ApplicationRespVO> applicationRespVOList;

    private List<ChangeRiskPlanRespVO> riskPlanRespVOList;


}
