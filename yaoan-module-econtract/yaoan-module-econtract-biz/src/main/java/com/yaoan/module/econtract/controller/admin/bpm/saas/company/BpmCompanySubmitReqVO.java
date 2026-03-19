package com.yaoan.module.econtract.controller.admin.bpm.saas.company;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-28 16:31
 */
@Data
public class BpmCompanySubmitReqVO {

    /**
     * 社会信用代码
     */
    private String creditCode;
    private String reason;
    private Long userId;
}
