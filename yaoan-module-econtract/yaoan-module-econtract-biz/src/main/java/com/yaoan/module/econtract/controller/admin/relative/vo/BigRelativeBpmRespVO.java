package com.yaoan.module.econtract.controller.admin.relative.vo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.FlowableConfigParamBaseRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmRespVO;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025/3/11 19:51
 */
@Data
public class BigRelativeBpmRespVO  extends FlowableConfigParamBaseRespVO {

    /**
     * 列表信息
     */
    private PageResult<RelativeBpmListBpmRespVO> pageResult;
}
