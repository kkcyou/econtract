package com.yaoan.module.econtract.service.workbench;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.amount.vo.ContractAmountRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.alert.WorkBenchMsgAlertRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.fastdraft.FastViaModelRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.statistic.ContractAmountStatisticsRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.statistic.ContractStatisticCountRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.statistic.TemplateExpirationReminderRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.statistic.WorkbenchStatisticRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.BigWorkBenchTaskRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.WorkBenchTaskCountRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.WorkBenchTaskListRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.WorkBenchTaskReqVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.trend.WorkBenchSignTrendRespVO;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/4 11:12
 */
public interface WorkBenchService {


    /**
     * 单位端_工作台_总统计接口
     * @return {@link WorkbenchStatisticRespVO }
     */
    WorkbenchStatisticRespVO workbenchStatistic(String type);


    /**
     * 单位端_工作台_快速起草_模板起草
     * @param size
     * @return {@link List }<{@link FastViaModelRespVO }>
     */
    List<FastViaModelRespVO> draftFastViaModel(Integer size);


    /**
     * 单位端_工作台_审批列表
     * @param reqVO
     * @return {@link BigWorkBenchTaskRespVO }
     */
    PageResult<WorkBenchTaskListRespVO> toDoTaskList(WorkBenchTaskReqVO reqVO);

    /**
     * 单位端_工作台_统计待办任务数量
     * @return {@link WorkBenchTaskCountRespVO }
     */
    WorkBenchTaskCountRespVO countToDoTaskList();
    /**
     * 单位端_工作台_消息通知
     *
     * @param size
     * @return <{@link List }<{@link WorkBenchMsgAlertRespVO }>>
     */
    List<WorkBenchMsgAlertRespVO> msgAlert(Integer size);

    /**
     * 单位端_工作台_合同签订趋势
     *
     * @return {@link CommonResult }<{@link WorkBenchSignTrendRespVO }>
     */
    WorkBenchSignTrendRespVO signTrend();

    ContractStatisticCountRespVO contractStatistics(Integer flag);

    List<ContractAmountStatisticsRespVO> contractAmountStatistics(Integer flag);

    List<TemplateExpirationReminderRespVO> templateExpirationReminder();
}
