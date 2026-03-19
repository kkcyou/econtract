package com.yaoan.module.econtract.service.payment.invoice;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoicePageReqVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentInvoiceDO;

import javax.validation.Valid;

public interface PaymentInvoiceService {
    /**
     * 创建发票信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInvoice(@Valid PaymentInvoiceSaveReqVO createReqVO);

    /**
     * 更新发票信息
     *
     * @param updateReqVO 更新信息
     */
    void updateInvoice(@Valid PaymentInvoiceSaveReqVO updateReqVO);

    /**
     * 删除发票信息
     *
     * @param id 编号
     */
    void deleteInvoice(String id);

    /**
     * 获得发票信息
     *
     * @param id 编号
     * @return 发票信息
     */
    PaymentInvoiceDO getInvoice(String id);

    /**
     * 获得发票信息分页
     *
     * @param pageReqVO 分页查询
     * @return 发票信息分页
     */
    PageResult<PaymentInvoiceDO> getInvoicePage(PaymentInvoicePageReqVO pageReqVO);
}
