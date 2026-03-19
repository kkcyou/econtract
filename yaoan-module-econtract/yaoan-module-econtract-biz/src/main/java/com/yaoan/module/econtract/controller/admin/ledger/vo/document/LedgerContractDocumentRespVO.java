package com.yaoan.module.econtract.controller.admin.ledger.vo.document;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/19 17:46
 */
@Data
public class LedgerContractDocumentRespVO {
    /**
     * 主合同
     */
    private MainContractRespVO mainContract;
    /**
     * 合同附件 list
     */
    private List<AttachmentRespVO> attachmentRespVOList;
    /**
     * 归档件
     */
    private FilingFileRespVO filingFileRespVO;

}
