package com.yaoan.module.econtract.controller.admin.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/15 9:57
 */
@Data
public class IdReqVO {
    /**
     * 主键id
     */
    private String id;

    /**
     * 主键id集合
     */
    private List<String> idList;

    /**
     * 说明理由
     */
    private String reason;

    /**
     * 说明理由
     * {@link com.yaoan.module.system.enums.config.AnnotationScanPermissionEnums}
     */
    private String flag;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 文件id
     */
    private Long fileId;

    private Integer status;
}
