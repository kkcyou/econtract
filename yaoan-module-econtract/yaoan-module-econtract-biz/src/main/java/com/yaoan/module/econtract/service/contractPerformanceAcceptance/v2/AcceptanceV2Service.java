package com.yaoan.module.econtract.service.contractPerformanceAcceptance.v2;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.AcceptancePageReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.AcceptanceRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.query.AcceptanceQueryRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.save.AcceptanceCreateReqVO;

import javax.validation.Valid;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/21 17:57
 */
public interface AcceptanceV2Service {

    PageResult<AcceptanceRespVO> page(@Valid AcceptancePageReqVO pageReqVO);


    String save(@Valid AcceptanceCreateReqVO reqVO);

    AcceptanceQueryRespVO get(String id);
}
