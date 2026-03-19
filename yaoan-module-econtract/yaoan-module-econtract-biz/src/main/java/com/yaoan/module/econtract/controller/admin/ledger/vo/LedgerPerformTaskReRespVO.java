package com.yaoan.module.econtract.controller.admin.ledger.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/19 17:58
 */
@Data
public class LedgerPerformTaskReRespVO {
    /**
     * 履约任务id
     */
    private String id;
    /**
     * 履约任务名
     */
    private String name;
    /**
     * 履约任务类型，新增必填，修改不填
     */
    private String perfTaskTypeId;
    /**
     * 履约任务类型名称
     */
    private String perfTaskTypeName;
    /**
     * 履约时间
     */
    private Date perfTime;
    /**
     * 完成时间
     */
    private Date confirmerTime;
    /**
     * 履约人id
     */
    private String confirmer;
    /**
     * 履约人名称
     */
    private String confirmerName;
    /**
     * 合同履约id
     */
    private String contractPerfId;
    /**
     * 履约任务状态编码
     */
    private Integer taskStatus;
    /**
     * 数量或金额
     */
    private Integer number;





}
