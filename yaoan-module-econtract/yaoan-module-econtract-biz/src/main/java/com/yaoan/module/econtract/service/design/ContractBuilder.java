package com.yaoan.module.econtract.service.design;


import com.yaoan.module.econtract.controller.admin.design.vo.ContractInfoVO;

/**
 * @author doujiale
 */
public interface ContractBuilder {


    /**
     * 构建合同基本信息
     *
     * @param contractId 合同ID
     * @return this
     */
    ContractBuilder buildBaseInfo(String contractId);

    /**
     * 构建相对方信息
     *
     * @param contractId 合同ID
     * @return this
     */
    ContractBuilder buildRelationInfo(String contractId);

    /**
     * 构建附件信息
     *
     * @param contractId 合同ID
     * @return this
     */
    ContractBuilder buildAttachmentInfo(String contractId);

    ContractInfoVO build();

    void remove();

}
