package com.yaoan.module.econtract.service.amount;

import com.yaoan.module.econtract.controller.admin.amount.vo.SearchYearReqVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.ContractAmountRespVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.ContractTypeSignedStatisticRespVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.alert.GPMallAlertListReqVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.alert.GPMallBigAlertListRespVO;

import java.util.List;

/**
 * @description: 合同签约金额
 * @author: Pele
 * @date: 2023/11/8 21:36
 */
public interface ContractAmountService {

    /**
     * 合同签约金额情况
     */
    List<ContractAmountRespVO> getContractAmount(SearchYearReqVO reqVO);

    /**
     * 合同签约类型 统计 展示(饼状图)
     */
    ContractTypeSignedStatisticRespVO contractTypeSignedStatistic(SearchYearReqVO vo);

    GPMallBigAlertListRespVO listAlert(GPMallAlertListReqVO vo);
}
