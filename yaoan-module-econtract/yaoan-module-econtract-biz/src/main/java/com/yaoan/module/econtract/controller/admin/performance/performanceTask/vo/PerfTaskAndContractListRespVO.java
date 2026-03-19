package com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo;

import lombok.Data;

import java.util.Date;

/**
 * 返回点击查看履约展示的列表数据
 */
@Data
public class PerfTaskAndContractListRespVO {
    /**
     * 合同编码
     */
    private String contractCode;
    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 履约任务id
     */
    private String id;
    /**
     * 合同履约id
     */
    private String contractPerfId;
    /**
     * 履约任务状态编码
     */
    private Integer taskStatus;
    /**
     * 履约任务状态名称
     */
    private String taskStatusName;

    /**
     * 履约任务类型，新增必填，修改不填
     */
    private String perfTaskTypeId;
    /**
     * 履约任务类型名称
     */
    private String perfTaskTypeName;
    /**
     * 履约任务名
     */
    private String name;
    /**
     * 履约时间
     */
    private Date perfTime;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 确认时间 id查有
     */
    private Date confirmerTime;
    /**
     * 创建者，目前使用 SysUser 的 id 编号
     */
    private String creator;
    /**
     * 创建者名称
     */
    private String creatorName;
    /**
     * 确认者id id查有
     */
    private String confirmer;
    /**
     * 确认者名称 id查有
     */
    private String confirmerName;
}
