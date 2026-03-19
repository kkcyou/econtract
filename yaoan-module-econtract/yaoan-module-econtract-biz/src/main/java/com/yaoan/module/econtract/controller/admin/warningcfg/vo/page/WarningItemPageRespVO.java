package com.yaoan.module.econtract.controller.admin.warningcfg.vo.page;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-1 15:05
 */
@Data
public class WarningItemPageRespVO {

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

}
