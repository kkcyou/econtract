package com.yaoan.module.econtract.service.payment.invoice;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoicePageReqVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentInvoiceDO;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentInvoiceMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

@Service
@Validated
public class PaymentInvoiceServiceImpl implements PaymentInvoiceService {

    @Resource
    private PaymentInvoiceMapper invoiceMapper;

    @Override
    public String createInvoice(PaymentInvoiceSaveReqVO createReqVO) {
        // 插入
        PaymentInvoiceDO invoice = BeanUtils.toBean(createReqVO, PaymentInvoiceDO.class);
        invoiceMapper.insert(invoice);
        // 返回
        return invoice.getId();
    }

    @Override
    public void updateInvoice(PaymentInvoiceSaveReqVO updateReqVO) {
        // 校验存在
        validateInvoiceExists(updateReqVO.getId());
        // 更新
        PaymentInvoiceDO updateObj = BeanUtils.toBean(updateReqVO, PaymentInvoiceDO.class);
        invoiceMapper.updateById(updateObj);
    }

    @Override
    public void deleteInvoice(String id) {
        // 校验存在
        validateInvoiceExists(id);
        // 删除
        invoiceMapper.deleteById(id);
    }

    private void validateInvoiceExists(String id) {
        if (invoiceMapper.selectById(id) == null) {
            throw exception(SYSTEM_ERROR,"找不到该发票信息");
        }
    }

    @Override
    public PaymentInvoiceDO getInvoice(String id) {
        return invoiceMapper.selectById(id);
    }

    @Override
    public PageResult<PaymentInvoiceDO> getInvoicePage(PaymentInvoicePageReqVO pageReqVO) {
        return invoiceMapper.selectPage(pageReqVO);
    }

}
