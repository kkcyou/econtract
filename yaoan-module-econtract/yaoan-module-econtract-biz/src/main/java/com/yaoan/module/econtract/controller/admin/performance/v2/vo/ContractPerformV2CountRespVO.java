package com.yaoan.module.econtract.controller.admin.performance.v2.vo;

import lombok.Data;

/**
 * @description: {@link com.yaoan.module.econtract.enums.ContractStatusEnums}
 * PERFORMANCE_CLOSURE(2000, "履约关闭"),
 * PERFORMING(2001, "履约中"),
 * PERFORMANCE_COMPLETE(2002, "履约完成"),
 * PERFORMANCE_RISK(2003, "履约风险"),
 * PERFORMANCE_RISK_DISPUTE(2004, "履约争议"),
 * PERFORMANCE_RISK_PAUSE(2005, "履约暂停"),
 * PERFORMANCE_RISK_EXTENSION(2006, "履约延期"),
 * PERFORMANCE_RISK_OVERDUE(2007, "履约逾期"),
 * @author: Pele
 * @date: 2024/10/22 11:46
 */
@Data
public class ContractPerformV2CountRespVO {

    /**
     * 待履约
     */
    private Long signCompleted;

    /**
     * 履约中
     */
    private Long performing;
    /**
     * 履约风险
     */
    private Long performanceRisk;
    /**
     * 履约完成
     */
    private Long performanceComplete;
    /**
     * 履约关闭
     */
    private Long performanceClosure;

}
