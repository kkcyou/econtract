package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import lombok.Data;

import java.util.List;

@Data
public class BatchPublishReqVO {
    /**
     * 发布0/取消发布1
     */
    private Integer publish;

    /**
     * idList
     */
    private List<String> idList;
}
