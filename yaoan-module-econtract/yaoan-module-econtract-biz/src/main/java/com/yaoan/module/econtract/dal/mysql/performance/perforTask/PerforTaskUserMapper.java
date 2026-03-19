package com.yaoan.module.econtract.dal.mysql.performance.perforTask;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.PerformanceTaskInfoRespVO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskUserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Mapper
public interface PerforTaskUserMapper extends BaseMapperX<PerfTaskUserDO> {
    /**
     * 根据履约任务id查询用户信息
     * @param
     * @return
     */
    default List<PerfTaskUserDO> selectListByTaskId(PerformanceTaskInfoRespVO taskInfoRespVO) {
        return    selectList(new LambdaQueryWrapperX<PerfTaskUserDO>().eqIfPresent(PerfTaskUserDO::getPerfTaskId, taskInfoRespVO.getId())
                .select(PerfTaskUserDO::getId,PerfTaskUserDO::getPerfTaskId,PerfTaskUserDO::getUserId));
    }
    /**
     * 根据履约任务ids查询用户信息
     * @param
     * @return
     */
    default List<PerfTaskUserDO> selectListByTaskIds( List<String> taskIds) {
        return    selectList(new LambdaQueryWrapperX<PerfTaskUserDO>().inIfPresent(PerfTaskUserDO::getPerfTaskId,taskIds)
                .select(PerfTaskUserDO::getId,PerfTaskUserDO::getPerfTaskId,PerfTaskUserDO::getUserId));
    }

    }

