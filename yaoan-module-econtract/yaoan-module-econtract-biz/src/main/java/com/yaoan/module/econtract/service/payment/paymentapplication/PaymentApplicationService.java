package com.yaoan.module.econtract.service.payment.paymentapplication;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.StatisticsAmountV2RespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.BigPaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationSaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.statisticsamount.StatisticsAmountRespVO;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/21 17:18
 */
public interface PaymentApplicationService {

    PageResult<PaymentApplicationListRespVO> listPaymentApplication(PaymentApplicationListReqVO vo);

    PageResult<PaymentApplicationListRespVO> getPaymentApplicationPage(PaymentApplicationListReqVO paymentPlanRepVO);

    StatisticsAmountRespVO statisticsAmount(PaymentApplicationListBpmReqVO vo);

    PaymentApplicationRespVO getPaymentApplication(PaymentApplicationReqVO vo);

    String savePaymentApplication(PaymentApplicationSaveReqVO vo);

    String saveCollectionApplication(PaymentApplicationSaveReqVO vo);

    String updatePaymentApplication(PaymentApplicationUpdateReqVO vo);

    String deletePaymentApplications(IdReqVO vo);

    String submitApprovePaymentApplication(PaymentApplicationSaveReqVO vo);
    
    BigPaymentApplicationListBpmRespVO getBpmToDoTaskPage(Long loginUserId, PaymentApplicationListBpmReqVO pageVO);

    BigPaymentApplicationListBpmRespVO getBpmDoneTaskPage(Long loginUserId, PaymentApplicationListBpmReqVO pageVO);

    BigPaymentApplicationListBpmRespVO getBpmAllTaskPage(Long loginUserId, PaymentApplicationListBpmReqVO pageVO);

    StatisticsAmountRespVO statisticsApproveAmount(PaymentApplicationListBpmReqVO vo);

    StatisticsAmountV2RespVO statisticsV2();
}
