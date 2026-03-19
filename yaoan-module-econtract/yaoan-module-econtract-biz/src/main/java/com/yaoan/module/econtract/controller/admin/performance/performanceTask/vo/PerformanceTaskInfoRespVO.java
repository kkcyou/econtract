package com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
public class PerformanceTaskInfoRespVO extends  PerformanceTaskCreateVO{
/**
 * 履约任务类型名称
 */
private String perfTaskTypeName;
    /**
     * 前置履约任务名称
     */
    private String beforTaskName;
    /**
     * 用户信息
     */
    private List<Map<String,Object>> userInfo;     //userId userName
    /**
     * 履约任务状态
     */
    private Integer taskStatus;
    /**
     * 履约任务状态名称
     */
    private String taskStatusName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 确认时间
     */
    private Date confirmTime;
    /**
     * 创建者，目前使用 SysUser 的 id 编号
     */
    private String creator;
    /**
     * 创建者名称
     */
    private String creatorName;
    /**
     * 确认者id
     */
    private String confirmer;
    /**
     * 确认者名称
     */
    private String confirmerName;
}
