package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.FlowableConfigParamBaseRespVO;
import lombok.Data;


@Data
public class BigPaymentApplicationListBpmRespVO extends FlowableConfigParamBaseRespVO {
    private static final long serialVersionUID = -3298887392718926478L;
    /**
     * 列表信息
     */
    private PageResult<PaymentApplicationListBpmRespVO> pageResult;
}
