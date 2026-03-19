package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import lombok.Data;

@Data
public class TemplateHistoryRespVO extends TemplateAllPermissionReqVo{
    private String creator;

    /**
     * 创建者名称
     */
    private String creatorName;

    /**
     * 流程实例的编号
     */
    private String processInstanceId;
}
