package com.yaoan.module.econtract.controller.admin.payment.deferred.vo;

import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.deferred.PaymentDeferredApplyDO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/8 16:57
 */
@Data
public class PaymentDeferredApplicationRespVO {

    /**
     * 基本信息
     */
    private PaymentDeferredInfoRespVO paymentDeferredInfoRespVO;

    /**
     * 合同信息
     */
    private ContractInfoV2RespVO contractInfoRespVO;

    private List<BusinessFileVO> fileRespVOList;
}
