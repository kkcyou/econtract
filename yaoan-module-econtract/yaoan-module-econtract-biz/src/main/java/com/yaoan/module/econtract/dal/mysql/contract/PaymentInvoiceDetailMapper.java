package com.yaoan.module.econtract.dal.mysql.contract;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceDetailPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentInvoiceDetailDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentInvoiceDetailMapper extends BaseMapperX<PaymentInvoiceDetailDO> {

    default PageResult<PaymentInvoiceDetailDO> selectPage(PaymentInvoiceDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PaymentInvoiceDetailDO>()
                .eqIfPresent(PaymentInvoiceDetailDO::getInvoiceId, reqVO.getInvoiceId())
                .likeIfPresent(PaymentInvoiceDetailDO::getProName, reqVO.getProName())
                .eqIfPresent(PaymentInvoiceDetailDO::getSpecMod, reqVO.getSpecMod())
                .eqIfPresent(PaymentInvoiceDetailDO::getPrice, reqVO.getPrice())
                .eqIfPresent(PaymentInvoiceDetailDO::getTaxRate, reqVO.getTaxRate())
                .eqIfPresent(PaymentInvoiceDetailDO::getTaxAmt, reqVO.getTaxAmt())
                .eqIfPresent(PaymentInvoiceDetailDO::getTaxPrice, reqVO.getTaxPrice())
                .orderByDesc(PaymentInvoiceDetailDO::getId));
    }

}