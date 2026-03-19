package com.yaoan.module.econtract.service.performance.perfTask;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfPageReqVO;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
public interface PerfTaskService {
  String  createPerfTask(PerformanceTaskCreateVO taskCreateVO);
  void removeById(String id);
  List<Map<String,Object>> queryPerfTaskById(String id,String perfTaskId);
  PerformanceTaskInfoRespVO queryPerfTaskInfoById(String id);
  List<PerformanceTaskInfoRespVO>  queryPerfTaskListById(String id);
  PerformanceTaskListRespVO queryContractPerfTaskListById(ContractPerfPageReqVO contractPerfPageReqVO);
  PageResult<PerfTaskAndContractListRespVO> queryContractPerfTaskList(ContractPerfPageReqVO contractPerfPageReqVO);
  void surePerformance(PerfTaskReqVO perfTaskReqVO)throws IOException ;
  void surePerformanceV2(PerfTaskReqVO perfTaskReqVO);
  List<PerfTaskAndContractListRespVO> queryPerfRemindTaskList( ) ;
  void continuePerformance(String contractPerfId);
}
