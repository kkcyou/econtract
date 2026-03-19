package com.yaoan.module.econtract.service.payment.invoice;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceDetailPageReqVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceDetailSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentInvoiceDetailDO;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentInvoiceDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

@Service
@Validated
public class PaymentInvoiceDetailServiceImpl implements PaymentInvoiceDetailService {

    @Resource
    private PaymentInvoiceDetailMapper invoiceDetailMapper;

    @Override
    public String createInvoiceDetail(PaymentInvoiceDetailSaveReqVO createReqVO) {
        // 插入
        PaymentInvoiceDetailDO invoiceDetail = BeanUtils.toBean(createReqVO, PaymentInvoiceDetailDO.class);
        invoiceDetailMapper.insert(invoiceDetail);
        // 返回
        return invoiceDetail.getId();
    }

    @Override
    public void updateInvoiceDetail(PaymentInvoiceDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateInvoiceDetailExists(updateReqVO.getInvoiceId());
        // 更新
        PaymentInvoiceDetailDO updateObj = BeanUtils.toBean(updateReqVO, PaymentInvoiceDetailDO.class);
        invoiceDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteInvoiceDetail(String id) {
        // 校验存在
        validateInvoiceDetailExists(id);
        // 删除
        invoiceDetailMapper.deleteById(id);
    }

    private void validateInvoiceDetailExists(String id) {
        if (invoiceDetailMapper.selectById(id) == null) {
            throw exception(SYSTEM_ERROR,"找不到该发票详情信息");
        }
    }

    @Override
    public PaymentInvoiceDetailDO getInvoiceDetail(String id) {
        return invoiceDetailMapper.selectById(id);
    }

    @Override
    public PageResult<PaymentInvoiceDetailDO> getInvoiceDetailPage(PaymentInvoiceDetailPageReqVO pageReqVO) {
        return invoiceDetailMapper.selectPage(pageReqVO);
    }

}