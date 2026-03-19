package com.yaoan.module.econtract.api.changxie.dto.cleandraft;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/18 15:43
 */
@Data
public class CXCleanDraftReqDTO {

    /**
     * 文档路径，必填
     */
    private String fileUrl;

    /**
     * 是否接受全部修订，必填
     */
    private Boolean acceptAllRevision;
}
