package com.yaoan.module.econtract.api.contract.dto.gcy;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025/1/20 19:32
 */
@Data
public class CancellationFileDTO {

    /**
     * 合同id
     */
    private String contractId;
    /**
     * 附件id
     */
    private Long fileId;

    /**
     * 退回理由
     */
    private String reason;
}
