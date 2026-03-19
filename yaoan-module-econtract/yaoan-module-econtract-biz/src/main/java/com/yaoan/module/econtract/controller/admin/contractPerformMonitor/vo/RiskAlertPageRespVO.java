package com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/18 21:08
 */
@Data
public class RiskAlertPageRespVO {
    /**
     * 风险提示id
     */
    private String id;

    /**
     * 风险名称
     */
    private String name;

    /**
     * 风险类型
     */
    private Integer type;

    /**
     * 风险类型str
     */
    private String typeStr;

    /**
     * 时间
     */
    private LocalDateTime createTime;

}
