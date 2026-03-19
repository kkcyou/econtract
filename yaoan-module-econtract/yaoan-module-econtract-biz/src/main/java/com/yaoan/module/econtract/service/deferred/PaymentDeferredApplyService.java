package com.yaoan.module.econtract.service.deferred;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.payment.deferred.vo.*;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.BigPaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/30 16:28
 */
public interface PaymentDeferredApplyService {

    String saveOrUpdate(PaymentDeferredApplySaveReqVO vo);

    String update(PaymentDeferredApplyUpdateReqVO vo);

    String delete(String id);

    /**
     * @param id 计划id
     * @return
     */
    PaymentDeferredApplicationRespVO query(String id);

    String submit(String id);

    BigPaymentApplicationListBpmRespVO getBpmAllTaskPage(Long loginUserId, PaymentApplicationListBpmReqVO pageVO);

    BigPaymentApplicationListBpmRespVO getBpmDoneTaskPage(Long loginUserId, PaymentApplicationListBpmReqVO pageVO);

    BigPaymentApplicationListBpmRespVO getBpmToDoTaskPage(Long loginUserId, PaymentApplicationListBpmReqVO pageVO);

    PageResult<PaymentDeferredListRespVO> listDeferredApplication(PaymentDeferredListReqVO vo);

    /**
     * 保存后提交
     */
    String save2submit(PaymentDeferredApplySaveReqVO vo);
}
