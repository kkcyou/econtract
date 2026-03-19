package com.yaoan.module.econtract.convert.performance.perfTask;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.ledger.vo.LedgerPerformTaskReRespVO;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.PerfTaskAndContractListRespVO;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.PerfTaskReqVO;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.PerformanceTaskCreateVO;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.PerformanceTaskInfoRespVO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Mapper(componentModel = "spring")
public interface PerfTaskConverter {
    PerfTaskConverter INSTANCE = Mappers.getMapper(PerfTaskConverter.class);
    PerfTaskDO toentry(PerformanceTaskCreateVO performanceTaskCreateVO);
    PerformanceTaskInfoRespVO toVO(PerfTaskDO perfTaskDO);
    List<PerformanceTaskInfoRespVO> toListVO(List<PerfTaskDO> perfTaskDOS);
    PageResult<PerfTaskAndContractListRespVO> toVOList(PageResult<PerfTaskDO> perfTaskDOS);
    PageResult<LedgerPerformTaskReRespVO> toVOList2(PageResult<PerfTaskDO> perfTaskDOS);
    List<PerfTaskAndContractListRespVO> toVOList(List<PerfTaskDO> perfTaskDOS);
    PerfTaskDO toVO(PerfTaskReqVO perfTaskReqVO);

    PageResult<LedgerPerformTaskReRespVO> toLegerPerTask(PageResult<PerfTaskDO> taskDOPageResult);
}


