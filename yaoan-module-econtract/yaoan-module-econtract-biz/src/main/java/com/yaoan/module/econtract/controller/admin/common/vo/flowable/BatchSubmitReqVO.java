package com.yaoan.module.econtract.controller.admin.common.vo.flowable;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/20 14:12
 */
@Data
public class BatchSubmitReqVO {

    /**
     * 业务流程标识
     */
    private String businessFlag;

    /**
     * 批量信息
     */
    private List<SubmitReqVO> submitReqList;
}
