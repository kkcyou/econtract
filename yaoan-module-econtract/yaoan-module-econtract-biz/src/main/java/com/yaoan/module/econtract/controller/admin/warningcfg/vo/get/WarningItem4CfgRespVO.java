package com.yaoan.module.econtract.controller.admin.warningcfg.vo.get;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025-6-30 13:41
 */
@Data
public class WarningItem4CfgRespVO {
    /**
     * 主键
     */
    private String id;
    /**
     * 检查点id
     */
    private String configId;
    /**
     * 预警事项名称
     */
    private String itemName;
    /**
     * 风险说明
     */
    private String itemRemark;

    private List<WarningItemRule4CfgRespVO> rules;
}
