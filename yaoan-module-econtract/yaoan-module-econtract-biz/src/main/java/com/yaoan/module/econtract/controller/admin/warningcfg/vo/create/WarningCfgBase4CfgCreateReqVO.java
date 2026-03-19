package com.yaoan.module.econtract.controller.admin.warningcfg.vo.create;

import lombok.Data;

import java.util.List;

/**
 * 预警检查配置表(new预警)
 *
 */
@Data
public class WarningCfgBase4CfgCreateReqVO {

    /**
     * 主键
     */
    private String id;
    /**
     * 检查点名称
     */
    private String name;
    /**
     * 模块来源id(方便前端取值，取最后一位)
     */
    private List<String> modelIds;
    /**
     * 模块来源id
     */
    private String modelId;
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
