package com.yaoan.module.econtract.dal.mysql.contract;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoicePageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentInvoiceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentInvoiceMapper extends BaseMapperX<PaymentInvoiceDO> {

    default PageResult<PaymentInvoiceDO> selectPage(PaymentInvoicePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PaymentInvoiceDO>()
                .eqIfPresent(PaymentInvoiceDO::getInvoiceCode, reqVO.getInvoiceCode())
                .eqIfPresent(PaymentInvoiceDO::getInvoiceNo, reqVO.getInvoiceNo())
                .likeIfPresent(PaymentInvoiceDO::getInvoiceTypeName, reqVO.getInvoiceTypeName())
                .eqIfPresent(PaymentInvoiceDO::getInvoiceTypeCode, reqVO.getInvoiceTypeCode())
                .eqIfPresent(PaymentInvoiceDO::getRemark, reqVO.getRemark())
                .likeIfPresent(PaymentInvoiceDO::getOtaxPayName, reqVO.getOtaxPayName())
                .eqIfPresent(PaymentInvoiceDO::getOtaxPayCode, reqVO.getOtaxPayCode())
                .likeIfPresent(PaymentInvoiceDO::getOtaxPayBankName, reqVO.getOtaxPayBankName())
                .eqIfPresent(PaymentInvoiceDO::getOtaxPayBankNo, reqVO.getOtaxPayBankNo())
                .eqIfPresent(PaymentInvoiceDO::getOtaxPayAddr, reqVO.getOtaxPayAddr())
                .eqIfPresent(PaymentInvoiceDO::getOtaxPayTel, reqVO.getOtaxPayTel())
                .likeIfPresent(PaymentInvoiceDO::getPtaxPayName, reqVO.getPtaxPayName())
                .eqIfPresent(PaymentInvoiceDO::getPtaxPayCode, reqVO.getPtaxPayCode())
                .likeIfPresent(PaymentInvoiceDO::getPtaxPayBankName, reqVO.getPtaxPayBankName())
                .eqIfPresent(PaymentInvoiceDO::getPtaxPayBankNo, reqVO.getPtaxPayBankNo())
                .eqIfPresent(PaymentInvoiceDO::getPtaxPayAddr, reqVO.getPtaxPayAddr())
                .eqIfPresent(PaymentInvoiceDO::getPtaxPayTel, reqVO.getPtaxPayTel())
                .eqIfPresent(PaymentInvoiceDO::getPayId, reqVO.getPayId())
                .betweenIfPresent(PaymentInvoiceDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(PaymentInvoiceDO::getUpdater, reqVO.getUpdater())
                .eqIfPresent(PaymentInvoiceDO::getCreator, reqVO.getCreator())
                .orderByDesc(PaymentInvoiceDO::getId));
    }

    default void insert(){

    }

}