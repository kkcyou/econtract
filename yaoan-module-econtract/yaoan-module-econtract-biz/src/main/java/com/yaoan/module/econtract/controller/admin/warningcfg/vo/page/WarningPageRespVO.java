package com.yaoan.module.econtract.controller.admin.warningcfg.vo.page;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-1 14:50
 */
@Data
public class WarningPageRespVO  {
    private String id;
    /**
     * 检查点名称
     */
    private String name;
    /**
     * 模块来源
     */
    private String modelId;

    /**
     * 模块编号
     * */
    private String modelCode;
    /**
     * 模块来源名称
     */
    private String modelName;
    /**
     * 启用状态
     */
    private String status;

    private List<WarningItemPageRespVO> itemPageRespVOList;

}
