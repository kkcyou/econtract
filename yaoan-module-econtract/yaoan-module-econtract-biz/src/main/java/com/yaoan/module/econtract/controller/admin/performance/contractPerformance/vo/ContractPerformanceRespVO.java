package com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo;

import lombok.Data;

import java.util.Date;

/**
 * ContractPerformanceRespVO
 */
@Data
public class ContractPerformanceRespVO {
    /**
     * 合同履约id
     */
    private String id;
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 合同编码
     */
    private String contractCode;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 签署完成时间
     */
    private Date signFinishTime;
    /**
     * 合同类型id
     */
    private String contractTypeId;
    /**
     * 合同类型名称
     */
    private String contractTypeName;

    /**
     * 合同状态编码
     */
    private Integer contractStatus;
    /**
     * 合同状态值
     */
    private  String contractStatusName;

    /**
     * 履约任务完成情况
     */
    private String finishInfo;

    /**
     * 履约最终截止时间，展示合同下所有履约任务中履约时间最靠后的时间
     */
    private Date perfTime;

}
