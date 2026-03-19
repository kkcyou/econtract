package com.yaoan.module.econtract.service.contractInvoiceManage;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo.ContractInvoiceManagePageReqVO;
import com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo.ContractInvoiceManageRespVO;
import com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo.ContractInvoiceManageSaveReqVO;

import javax.validation.Valid;


/**
 * 发票 Service 接口
 *
 * @author lls
 */
public interface ContractInvoiceManageService {

    /**
     * 创建发票
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createContractInvoiceManage(@Valid ContractInvoiceManageSaveReqVO createReqVO);

    /**
     * 更新发票
     *
     * @param updateReqVO 更新信息
     */
    void updateContractInvoiceManage(@Valid ContractInvoiceManageSaveReqVO updateReqVO);

    /**
     * 删除发票
     *
     * @param id 编号
     */
    void deleteContractInvoiceManage(String id);

    /**
     * 获得发票
     *
     * @param id 编号
     * @return 发票
     */
    ContractInvoiceManageRespVO getContractInvoiceManage(String id);

    /**
     * 获得发票分页
     *
     * @param pageReqVO 分页查询
     * @return 发票分页
     */
    PageResult<ContractInvoiceManageRespVO> getContractInvoiceManagePage(ContractInvoiceManagePageReqVO pageReqVO);

}