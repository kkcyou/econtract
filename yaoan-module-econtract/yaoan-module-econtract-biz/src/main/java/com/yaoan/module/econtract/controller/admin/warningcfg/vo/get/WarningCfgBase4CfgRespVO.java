package com.yaoan.module.econtract.controller.admin.warningcfg.vo.get;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025-6-30 13:40
 */
@Data
public class WarningCfgBase4CfgRespVO {

    /**
     * 主键
     */
    private String id;
    /**
     * 检查点名称
     */
    private String name;

    private String modelId;

    private List<String> modelIds;
    /**
     * 模块来源
     */
    private String modelCode;
    /**
     * 模块来源名称
     */
    private String modelName;
    /**
     * 启用状态
     */
    private String status;
}
