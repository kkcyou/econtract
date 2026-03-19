package com.yaoan.module.econtract.convert.payment.deferred;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.payment.deferred.vo.PaymentDeferredApplySaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.deferred.vo.PaymentDeferredApplyUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.payment.deferred.vo.PaymentDeferredInfoRespVO;
import com.yaoan.module.econtract.controller.admin.payment.deferred.vo.PaymentDeferredListRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.deferred.PaymentDeferredApplyDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/30 16:10
 */
@Mapper
public interface PaymentDeferredApplyConverter {
    PaymentDeferredApplyConverter INSTANCE = Mappers.getMapper(PaymentDeferredApplyConverter.class);


    PaymentDeferredApplyDO saveReq2D(PaymentDeferredApplySaveReqVO vo);

    PaymentDeferredApplyDO updateReq2D(PaymentDeferredApplyUpdateReqVO vo);

    List<PaymentApplicationListBpmRespVO> convertBpmDO2Resp(List<PaymentDeferredApplyDO> doList);
    @Mapping(target = "paymentApplyCode", source = "code")
    @Mapping(target = "applicantId", source = "creator")
    PaymentApplicationListBpmRespVO convertBpmDO2R(PaymentDeferredApplyDO doList);

    PageResult<PaymentDeferredListRespVO> convertPageDO2Resp(PageResult<PaymentDeferredApplyDO> paymentApplicationDOList);
    @Mapping(target = "applyUserId", source = "creator")
    PaymentDeferredListRespVO convertPageDO2(PaymentDeferredApplyDO paymentApplicationDOList);
    @Mapping(target = "applicantId", source = "applyUserId")
    @Mapping(target = "applicantName", source = "applyUserName")
    @Mapping(target = "paymentApplyCode", source = "code")
    PaymentDeferredInfoRespVO convertDO2InfoResp(PaymentDeferredApplyDO applicationDO);
}
