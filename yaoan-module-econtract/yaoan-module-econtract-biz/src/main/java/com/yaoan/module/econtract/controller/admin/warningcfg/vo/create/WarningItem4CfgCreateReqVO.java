package com.yaoan.module.econtract.controller.admin.warningcfg.vo.create;

import lombok.Data;

import java.util.List;

/**
 * 预警事项表（new预警）
 * */
@Data
public class WarningItem4CfgCreateReqVO {

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

    private List<WarningItemRule4CfgCreateReqVO> rules;
}
