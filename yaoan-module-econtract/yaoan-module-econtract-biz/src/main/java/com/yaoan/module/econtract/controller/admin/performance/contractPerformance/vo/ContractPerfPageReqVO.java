package com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo;

import cn.hutool.core.date.DateTime;
import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;
@Data
public class ContractPerfPageReqVO extends PageParam {
    /**
     * 合同状态
     */
    private Integer contractStatus;
    /**
     * 合同类型
     */
    private String contractTypeId;
    /**
     * 履约任务类型id
     */
    private String perfTaskTypeId;
    /**
     * 履约截止时间头
     */
    private DateTime startFinishTime;
    /**
     * 履约截止时间尾
     */
    private DateTime endFinishTime;
    /**
     * 签署完成结束时间
     */
    private DateTime endSignTime;

    /**
     * 签署完成开始时间
     */
    private DateTime startSignTime;
    /**
     * 搜索字符串
     */
    private String searchText;
    /**
     * 合同履约id
     */
    private String id;
    /**
     * 履约开始时间
     */
    private DateTime startTime;
    /**
     * 履约结束时间
     */
    private DateTime endTime;
    /**
     * 履约任务状态编码
     */
    private Integer taskStatus;
    /**
     * 履约开始提醒时间
     */
    private DateTime startRemindTime;
    /**
     * 履约结束提醒时间
     */
    private DateTime endRemindTime;
    /**
     * flag=all  用于标记是点击查看更多进入   week为查本周数据
     */
    private  String flag;
}
