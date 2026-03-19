package com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * PerformanceTaskCreateVO
 */
@Data
public class PerformanceTaskCreateVO extends  BasePerformanceTask{
    /**
     * 履约任务id，新增没有，修改必有
     */
    private String id;

}
