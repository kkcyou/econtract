package com.yaoan.module.econtract.controller.admin.contract.vo.ledger;

import com.yaoan.module.infra.api.file.dto.FileDTO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025-5-19 17:08
 */
@Data
public class ContractFileRespVO {
    /**
     * 正文
     * */
    private Long txtFileId;
    private String txtFileUrl;
    private String txtFileName;

    /**
     * 线下签署-合同文件
     * */
    private Long OfflineDraftFileId;
    private String OfflineDraftFileUrl;
    private String OfflineDraftFileName;


    /**
     * 线下签署-审批材料
     * */
    private Long OfflineApproveFileId;
    private String OfflineApproveFileUrl;
    private String OfflineApproveFileName;

    /**
     * 合同附件
     * */
    private  List<FileDTO> attachmentFileVOs;

}
