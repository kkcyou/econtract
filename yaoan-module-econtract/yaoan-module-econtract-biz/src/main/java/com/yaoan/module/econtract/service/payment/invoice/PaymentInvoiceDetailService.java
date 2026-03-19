package com.yaoan.module.econtract.service.payment.invoice;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceDetailPageReqVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceDetailSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentInvoiceDetailDO;

import javax.validation.Valid;

public interface PaymentInvoiceDetailService {
    /**
     * 创建发票信息明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInvoiceDetail(@Valid PaymentInvoiceDetailSaveReqVO createReqVO);

    /**
     * 更新发票信息明细
     *
     * @param updateReqVO 更新信息
     */
    void updateInvoiceDetail(@Valid PaymentInvoiceDetailSaveReqVO updateReqVO);

    /**
     * 删除发票信息明细
     *
     * @param id 编号
     */
    void deleteInvoiceDetail(String id);

    /**
     * 获得发票信息明细
     *
     * @param id 编号
     * @return 发票信息明细
     */
    PaymentInvoiceDetailDO getInvoiceDetail(String id);

    /**
     * 获得发票信息明细分页
     *
     * @param pageReqVO 分页查询
     * @return 发票信息明细分页
     */
    PageResult<PaymentInvoiceDetailDO> getInvoiceDetailPage(PaymentInvoiceDetailPageReqVO pageReqVO);

}
