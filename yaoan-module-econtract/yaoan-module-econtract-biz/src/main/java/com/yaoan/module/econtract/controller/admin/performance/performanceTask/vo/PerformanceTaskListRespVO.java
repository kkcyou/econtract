package com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo;

import lombok.Data;

import java.util.List;

/**
 * 返回点击查看履约展示的列表数据
 */
@Data
public class PerformanceTaskListRespVO {
    /**
     * 合同编码
     */
    private String contractCode;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同状态码值
     */
    private Integer contractStatus;
    /**
     * 合同状态中文值
     */
    private String contractStatusName;
    /**
     * 履约任务完成情况 id查有
     */
    private String finishInfo;
    /**
     * 合同履约id
     */
    private String contractPerfId;

List<PerfTaskAndContractListRespVO> perfTaskAndContractListRespVOList;
}
