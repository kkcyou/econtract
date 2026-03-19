package com.yaoan.module.econtract.dal.mysql.performance.perforTask;

import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.query.MPJQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import java.time.LocalDate;
import java.util.List;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Mapper
public interface PerforTaskMapper extends BaseMapperX<PerfTaskDO> {
    /**
     * 查询同一履约下的正在履约中的任务
     * @param contractPerfId
     * @return
     */
    default PerfTaskDO queryPerfTime(String contractPerfId, Integer status) {
        return selectOne(new LambdaQueryWrapperX<PerfTaskDO>()
                .eqIfPresent(PerfTaskDO::getContractPerfId, contractPerfId)
                .eqIfPresent(PerfTaskDO::getTaskStatus, status)

        );
    }

    /**
     * 查询同一履约下履约时间在哪最后的履约任务
     */
    default PerfTaskDO queryLastPerfTask(String contractPerfId, Integer status, String order, String id) {
        LambdaQueryWrapperX<PerfTaskDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.neIfPresent(PerfTaskDO::getId, id);
        if ("asc".equals(order)) {
            //查询最近的履约任务
            queryWrapperX.orderByAsc(PerfTaskDO::getPerfTime);
        }
        if ("desc".equals(order)) {
            //查询最后一个履约任务
            queryWrapperX.orderByDesc(PerfTaskDO::getPerfTime);
        }
        //根据任务状态
        queryWrapperX.eqIfPresent(PerfTaskDO::getTaskStatus, status)
                .eqIfPresent(PerfTaskDO::getContractPerfId, contractPerfId)
                .last("limit 1");
        return selectOne(queryWrapperX);
    }

    /**
     * 履约监控列表展示任务信息
     * @param contractPerfPageReqVO
     * @param firstDayOfWeek
     * @param lastDayOfWeek
     * @return
     */
    default PageResult<PerfTaskDO> queryContractPerfTaskList(ContractPerfPageReqVO contractPerfPageReqVO, LocalDate firstDayOfWeek, LocalDate lastDayOfWeek, String orderType) {
        MPJQueryWrapper<PerfTaskDO> mpjQueryWrapper = new MPJQueryWrapper<PerfTaskDO>().selectAll(PerfTaskDO.class)
                .leftJoin("ecms_contract_perf c on c.id = t.contract_perf_id");
        if ("asc".equals(orderType)) {
            //升序（履约时间从小到大）
            mpjQueryWrapper.orderByAsc("t.perf_time");
        }
        if ("desc".equals(orderType)) {
            //降序（履约时间从大到小）
            mpjQueryWrapper.orderByDesc("t.perf_time");
        }
        //根据履约合同id查询
        if (StringUtils.isNotBlank(contractPerfPageReqVO.getId())) {
            mpjQueryWrapper.eq("t.contract_perf_id", contractPerfPageReqVO.getId());
        }
        //查询一周的任务
        if (ObjectUtil.isNotEmpty(firstDayOfWeek) && ObjectUtil.isNotEmpty(lastDayOfWeek)) {
            mpjQueryWrapper.between("t.perf_time", firstDayOfWeek, lastDayOfWeek);
        }
        //履约时间段
        if (ObjectUtil.isNotEmpty(contractPerfPageReqVO.getStartTime()) && ObjectUtil.isNotEmpty(contractPerfPageReqVO.getEndTime())) {
            mpjQueryWrapper.between("t.perf_time", contractPerfPageReqVO.getStartTime().toDateStr(), contractPerfPageReqVO.getEndTime().toDateStr());
        }
        //提醒时间段
        if (ObjectUtil.isNotEmpty(contractPerfPageReqVO.getStartRemindTime()) && ObjectUtil.isNotEmpty(contractPerfPageReqVO.getEndRemindTime())) {
            mpjQueryWrapper.between("t.remind_time", contractPerfPageReqVO.getStartRemindTime().toDateStr(), contractPerfPageReqVO.getEndRemindTime().toDateStr());
        }
        //任务状态
        if (ObjectUtil.isNotEmpty(contractPerfPageReqVO.getTaskStatus())) {
            mpjQueryWrapper.eq("t.task_status", contractPerfPageReqVO.getTaskStatus());
        }
//履约任务类型
        if (StringUtils.isNotEmpty(contractPerfPageReqVO.getPerfTaskTypeId())) {
            mpjQueryWrapper.eq("t.perf_task_type_id", contractPerfPageReqVO.getPerfTaskTypeId());
        }
        // 合同类型
        if (StringUtils.isNotEmpty(contractPerfPageReqVO.getContractTypeId())) {
            mpjQueryWrapper.eq("c.contract_type_id", contractPerfPageReqVO.getContractTypeId());
        }
        //搜索字符串 匹配合同编码，合同名称，履约负责人
        if (StringUtils.isNotBlank(contractPerfPageReqVO.getSearchText())) {
            mpjQueryWrapper.like("c.contract_code", contractPerfPageReqVO.getSearchText())
                    .or().like("c.contract_name", contractPerfPageReqVO.getSearchText());
        }
        return selectPage(contractPerfPageReqVO, mpjQueryWrapper);
    }
    /**
     * 查询同一履约下的正在履约中的任务
     * @param id
     * @return
     */
    default List<PerfTaskDO> selectListById(String id) {
        return selectList(new LambdaQueryWrapperX<PerfTaskDO>().eq(PerfTaskDO::getContractPerfId, id).select(PerfTaskDO::getId,PerfTaskDO::getName,PerfTaskDO::getPerfTime));
    }







}
