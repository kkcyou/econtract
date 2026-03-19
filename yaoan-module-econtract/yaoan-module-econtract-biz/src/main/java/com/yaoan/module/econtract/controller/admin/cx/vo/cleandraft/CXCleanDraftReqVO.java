package com.yaoan.module.econtract.controller.admin.cx.vo.cleandraft;

import lombok.Data;

/**
 * @description: 一键清稿（接受全部修订、删除全部批注）请求参数
 * @author: Pele
 * @date: 2024/12/18 15:35
 */
@Data
public class CXCleanDraftReqVO {

    /**
     * 文档路径，必填
     */
    private String fileUrl;

    /**
     * 是否接受全部修订，必填
     */
    private Boolean acceptAllRevision;
}
