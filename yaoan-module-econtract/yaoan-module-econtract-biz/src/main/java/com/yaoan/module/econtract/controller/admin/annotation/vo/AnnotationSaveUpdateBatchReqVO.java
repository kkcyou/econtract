package com.yaoan.module.econtract.controller.admin.annotation.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/28 9:51
 */
@Data
public class AnnotationSaveUpdateBatchReqVO {
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 业务id
     */
    private String businessId;
    /**
     * 批注信息
     */
    private List<AnnotationSaveUpdateReqVO> reqVOS;
}
