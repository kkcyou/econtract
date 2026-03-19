package com.yaoan.module.econtract.convert.payment;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationSaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.ContractInfoRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.PaymentApplicationBaseInfoRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.PaymentPlanRespVO;
import com.yaoan.module.econtract.controller.admin.performance.v2.vo.PerformV2SaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangePaymentDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentRecordRespVO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/22 10:19
 */
@Mapper
public interface PaymentApplicationConverter {
    PaymentApplicationConverter INSTANCE = Mappers.getMapper(PaymentApplicationConverter.class);


    PageResult<PaymentApplicationListRespVO> convertPageDO2Resp(PageResult<PaymentApplicationDO> paymentApplicationDOList);

    PaymentApplicationListRespVO convertDO2Resp(PaymentApplicationDO paymentApplication);

    List<PaymentApplicationListRespVO> convertListDOResp(List<PaymentApplicationDO> list);

    PaymentApplicationBaseInfoRespVO convertVO2BaseInfo(PaymentApplicationReqVO vo);

    List<PaymentPlanRespVO> convertListSchedule2Resp(  List<PaymentScheduleDO> entity);

    PaymentPlanRespVO convertSchedule2Resp (PaymentApplicationReqVO vo);

    PageResult<PaymentApplicationListRespVO> convertDO2Resp(PageResult<PaymentApplicationDO> paymentApplicationDOList);
    PageResult<PaymentRecordRespVO> toPageVO(PageResult<PaymentApplicationDO> paymentApplicationDOList);

    PaymentApplicationDO convertSaveVO2DO(PaymentApplicationSaveReqVO vo);

    PaymentApplicationDO convertUpdateVO2DO(PaymentApplicationUpdateReqVO vo);

    List<PaymentApplicationListBpmRespVO> convertBpmDO2Resp(List<PaymentApplicationDO> doList);

    PaymentApplicationBaseInfoRespVO convertDO2InfoResp(PaymentApplicationDO applicationDO);

    PaymentScheduleDO r2D(PerformV2SaveReqVO reqVO);
}
