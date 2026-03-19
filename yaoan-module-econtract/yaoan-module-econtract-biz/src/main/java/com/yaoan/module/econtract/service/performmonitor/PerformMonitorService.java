package com.yaoan.module.econtract.service.performmonitor;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo.*;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.statisticsamount.StatisticsAmountRespVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/5 21:07
 */
public interface PerformMonitorService {



    List<PayableBigRespVO> countPayable(PayableReqVo vo);

    List<ContractPerformOverviewRespVo> overviewContractPerform();

    List<PerformTaskStatisticRespVo> performTaskStatistic();

    List<RiskAlertRespVO> listTodayRiskAlert();

    PageResult<RiskAlertPageRespVO> listMoreRiskAlert(RiskAlertPageReqVO vo);

    void exportExcel(PayableReqVo vo, HttpServletResponse response) throws IOException;

    StatisticsAmountRespVO statisticsAmount(PaymentApplicationListBpmReqVO vo);
}
