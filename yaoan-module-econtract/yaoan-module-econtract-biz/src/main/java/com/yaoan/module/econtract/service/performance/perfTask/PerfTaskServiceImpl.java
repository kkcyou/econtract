package com.yaoan.module.econtract.service.performance.perfTask;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfPageReqVO;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.*;
import com.yaoan.module.econtract.convert.performance.perfTask.PerfTaskConverter;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskUserDO;
import com.yaoan.module.econtract.dal.dataobject.performtasktype.PerformTaskTypeDO;
import com.yaoan.module.econtract.dal.mysql.performTaskType.PerformTaskTypeMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskUserMapper;
import com.yaoan.module.econtract.enums.ContractPerfEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.PerfTaskEnums;
import com.yaoan.module.econtract.service.performance.contractPerformance.ContractPerfService;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import dm.jdbc.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */

@Service
public class PerfTaskServiceImpl implements PerfTaskService {
    @Resource
    private ContractPerforMapper contractPerforMapper;
    @Resource
    private PerforTaskMapper perforTaskMapper;
    @Resource
    private PerforTaskUserMapper taskUserMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private FileApi fileApi;
    @Resource
    private PerformTaskTypeMapper taskTypeMapper;
    @Resource
    private PerfTaskConverter perfTaskConverter;
    @Resource
    private ContractPerfService contractPerfService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPerfTask(PerformanceTaskCreateVO taskCreateVO) {

        //使用履约id查合同生效时间
        ContractPerformanceDO contractPerformanceDO = contractPerforMapper.selectOne(ContractPerformanceDO::getId, taskCreateVO.getContractPerfId());
        //1.将vo转换成实体类
        PerfTaskDO entity = perfTaskConverter.toentry(taskCreateVO);
        //2.设置履约状态
        //2.1.有合同开始时间和结束时间--合同为存量合同
        if (ObjectUtil.isNotEmpty(contractPerformanceDO == null ? null : contractPerformanceDO.getValidity0())) {
            //合同开始时间
            //创建时间在合同生效时间之前--履约任务均为“未开始
            if (! DateUtil.beginOfDay(new Date()).isAfter(contractPerformanceDO == null ? null : contractPerformanceDO.getValidity0())) {
                entity.setTaskStatus(PerfTaskEnums.PERFTASK_NO_START.getCode());
            } else {
                //如果有合同结束时间
                if (ObjectUtil.isNotEmpty(contractPerformanceDO == null ? null : contractPerformanceDO.getValidity1())) {
                    //合同结束时间
                    //在有效期内：进入履约管理，合同状态为“待建立履约任务”，建立履约任务后，履约任务时间排在第一个的，为履约中，其他为待履约
                    if (taskCreateVO.getPerfTime().after(contractPerformanceDO == null ? null : contractPerformanceDO.getValidity0()) && taskCreateVO.getPerfTime().before(contractPerformanceDO == null ? null : contractPerformanceDO.getValidity1())) {
                        setPerfStatus(entity);
                    } else {
                        throw exception(ErrorCodeConstants.PERFORMANCE_TIME_ERROR2);
                    }
                } else {
                    //合同结束时间为空，只需要判断履约时间在合同开始时间后即可
                    if (taskCreateVO.getPerfTime().before(contractPerformanceDO == null ? null : contractPerformanceDO.getValidity0())) {
                        throw exception(ErrorCodeConstants.PERFORMANCE_TIME_ERROR);
                    }
                    setPerfStatus(entity);
                }
            }
        } else {
            //2.设置履约状态--合同为系统签约合同
            setPerfStatus(entity);
        }
        //3.校验前置任务履约时间是否在新增履约任务的履约时间之前
        String beforeTaskId = entity.getBeforTaskId();
        //不等于空表示有前置任务
        if (StringUtil.isNotEmpty(beforeTaskId)) {
            PerfTaskDO perfTaskDO = perforTaskMapper.selectById(beforeTaskId);
            if (BeanUtil.isNotEmpty(perfTaskDO)) {
                if (!perfTaskDO.getPerfTime().before(entity.getPerfTime())) {
                    throw exception(ErrorCodeConstants.POSTTASK_TO_early);
                }
            }
        }
        //5.将最终时间入履约表并修改履约状态
        Date bigTime = isBigTime(entity);
        ContractPerformanceDO contractPerformanceDO1 = new ContractPerformanceDO().setId(entity.getContractPerfId()).setPerfTime(bigTime);
        //设置履约合同状态
        if (!PerfTaskEnums.WAIT_PERFORMANCE.getCode().equals(entity.getTaskStatus())) {
            //履约状态不为待履约时，将履约合同状态与履约任务状态保持一致
            contractPerformanceDO1.setContractStatus(entity.getTaskStatus());
        }
        contractPerforMapper.updateById(contractPerformanceDO1);

        //6.校验是否可编辑和删除
        if (StringUtil.isNotEmpty(entity.getId())) {
            //履约暂停、履约结束、履约完成的履约任务不可编辑及删除(超期同理)，仅有履约中，待履约，履约任务未开始可以删除
            isDeleteOrUpdate(entity.getId());
            //修改履约任务
            perforTaskMapper.updateById(entity);
            //删除用户表中的该条履约任务对应的负责人
            taskUserMapper.delete(new LambdaQueryWrapperX<PerfTaskUserDO>().eqIfPresent(PerfTaskUserDO::getPerfTaskId, entity.getId()));
        } else {
            //对履约任务类型和履约任务名称进行非空校验
            if (StringUtil.isEmpty(entity.getPerfTaskTypeId())) {
                throw exception(ErrorCodeConstants.PERFORM_TASK_TYPE_IS_NULL);
            }
            if (StringUtil.isEmpty(entity.getName())) {
                throw exception(ErrorCodeConstants.PERFORM_TASK_NAME_IS_NULL);
            }
            //新增履约任务
            perforTaskMapper.insert(entity);
        }
        //7 .负责人id入负责人履约任务中间表
        if (CollUtil.isNotEmpty(taskCreateVO.getUserIds())) {
            ArrayList<PerfTaskUserDO> perfTaskUserDOS = new ArrayList<>();
            for (Long userId : taskCreateVO.getUserIds()) {
                PerfTaskUserDO perfTaskUserDO = new PerfTaskUserDO();
                perfTaskUserDO.setUserId(userId).setPerfTaskId(entity.getId());
                perfTaskUserDOS.add(perfTaskUserDO);
            }
            taskUserMapper.insertBatch(perfTaskUserDOS);
        }
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(String id) {
        //1.履约暂停、履约结束、履约完成的履约任务不可编辑及删除(超期同理)，仅有履约中，待履约，履约任务未开始可以删除
        isDeleteOrUpdate(id);
        PerfTaskDO perfTaskDO = perforTaskMapper.selectById(id);
        //2.删除履约任务
        perforTaskMapper.deleteById(id);
        //3.修改合同履约表中最终履约时间
        if (BeanUtil.isNotEmpty(perfTaskDO)) {
            PerfTaskDO perfTaskDO1 = perforTaskMapper.queryLastPerfTask(perfTaskDO.getContractPerfId(), null, "desc", null);
            if (BeanUtil.isNotEmpty(perfTaskDO1)) {
                ContractPerformanceDO contractPerformanceDO = contractPerforMapper.selectContractPerforById(perfTaskDO.getContractPerfId());
                if (contractPerformanceDO.getPerfTime().before(perfTaskDO1.getPerfTime())) {
                    contractPerforMapper.updateById(new ContractPerformanceDO().setId(perfTaskDO.getContractPerfId()).setPerfTime(perfTaskDO1.getPerfTime()));
                }
            }
            //4.删除负责人
            //删除用户表中的该条履约任务对应的负责人
            taskUserMapper.delete(new LambdaQueryWrapperX<PerfTaskUserDO>().eqIfPresent(PerfTaskUserDO::getPerfTaskId, id));
            //5.修改履约任务状态
            List<PerfTaskDO> perfTaskDOS = perforTaskMapper.selectList(new LambdaQueryWrapperX<PerfTaskDO>().eqIfPresent(PerfTaskDO::getContractPerfId, perfTaskDO.getContractPerfId()));
            if (CollUtil.isNotEmpty(perfTaskDOS)) {
                //5.有多条履约任务,删除的任务为履约中
                if (PerfTaskEnums.IN_PERFORMANCE.getCode().equals(perfTaskDO.getTaskStatus())) {
                    //将履约时间最前面的待履约任务改为履约中
                    PerfTaskDO perfTaskDO2 = perforTaskMapper.queryLastPerfTask(perfTaskDO.getContractPerfId(), PerfTaskEnums.WAIT_PERFORMANCE.getCode(), "asc", null);
                    if (BeanUtil.isNotEmpty(perfTaskDO2)) {
                        perfTaskDO2.setTaskStatus(PerfTaskEnums.IN_PERFORMANCE.getCode());
                        perforTaskMapper.updateById(perfTaskDO2);
                    }
                }
            } else {
                //6.删除的是唯一的任务，修改履约状态为待建立履约
                contractPerforMapper.updateById(new ContractPerformanceDO().setId(perfTaskDO.getContractPerfId())
                        .setContractStatus(ContractPerfEnums.WAIT_CREATE_PERFORMANCE.getCode())
                        .setPerfTime(null));
            }


        }

    }

    @Override
    public List<Map<String, Object>> queryPerfTaskById(String id, String perfTaskId) {
        List<Map<String, Object>> perfTaskDOS = new ArrayList<>();
        List<PerfTaskDO> perfTaskDOS1 = perforTaskMapper.selectListById( id);
        for (PerfTaskDO perfTaskDO : perfTaskDOS1) {
            if (StringUtil.isEmpty(perfTaskId) || (StringUtil.isNotEmpty(perfTaskId) && !perfTaskDO.getId().equals(perfTaskId))) {
                //若前端传递任务id则返回的前置任务list过滤此任务信息
                Map<String, Object> map = new HashMap<>();
                map.put("id", perfTaskDO.getId());
                map.put("name", perfTaskDO.getName());
                map.put("perfTime", perfTaskDO.getPerfTime());
                perfTaskDOS.add(map);
            }
        }
        return perfTaskDOS;
    }

    @Override
    public PerformanceTaskInfoRespVO queryPerfTaskInfoById(String id) {
        PerfTaskDO perfTaskDO = perforTaskMapper.selectById(id);
        //1。将实体类转换成vo
        PerformanceTaskInfoRespVO taskInfoRespVO = perfTaskConverter.toVO(perfTaskDO);
        List<PerfTaskUserDO> perfTaskUserDOS = taskUserMapper.selectListByTaskId(taskInfoRespVO);
        Map<Long, AdminUserRespDTO> taskUserMap1 = getTaskUserMap1(perfTaskUserDOS);
        Map<String, List<Long>> taskUserMap2 = getTaskUserMap2(perfTaskUserDOS);
        //2.设置履约任务类型名称,前置履约任务名称,用户信息,创建人名称
        if (BeanUtil.isNotEmpty(taskInfoRespVO)) {
            setPerformanceTaskInfoRespVO(taskInfoRespVO,null,null,taskUserMap1,taskUserMap2);
        //3.设置任务状态名称
        taskInfoRespVO.setTaskStatusName(setTaskStatusName(taskInfoRespVO.getTaskStatus()));
        }

        return taskInfoRespVO;
    }

    @Override
    public List<PerformanceTaskInfoRespVO> queryPerfTaskListById(String id) {
        List<PerfTaskDO> perfTaskDOS = perforTaskMapper.selectList(new LambdaQueryWrapperX<PerfTaskDO>().eq(PerfTaskDO::getContractPerfId, id).orderByAsc(PerfTaskDO::getPerfTime));

        //1。将实体类转换成vo
        List<PerformanceTaskInfoRespVO> taskInfoRespVOs = perfTaskConverter.toListVO(perfTaskDOS);
        //获取履约任务类型map
        List<String> perfTaskTypeIds = taskInfoRespVOs.stream().map(PerformanceTaskInfoRespVO::getPerfTaskTypeId).collect(Collectors.toList());
        Map<String, PerformTaskTypeDO> taskTypeDOMap = getPerformTaskTypeDOMap(perfTaskTypeIds);
        //获取履约任务map
        Map<String, PerfTaskDO> perfTaskDOMap = getPerfTaskDOMap(id);
        //获取履约任务负责人用户map
           //获取所有履约任务id
        List<String> taskIds = taskInfoRespVOs.stream().map(PerformanceTaskInfoRespVO::getId).collect(Collectors.toList());
          //根据任务id获取user
        List<PerfTaskUserDO> perfTaskUserDOS = taskUserMapper.selectListByTaskIds( taskIds);
        Map<String, List<Long>> taskUserMap2 = getTaskUserMap2(perfTaskUserDOS);
        Map<Long, AdminUserRespDTO> taskUserMap1 = getTaskUserMap1(perfTaskUserDOS);
        for (PerformanceTaskInfoRespVO perfTaskvo : taskInfoRespVOs) {
            //2.设置履约任务类型名称,前置履约任务名称,用户信息,创建人名称
            setPerformanceTaskInfoRespVO(perfTaskvo,taskTypeDOMap,perfTaskDOMap,taskUserMap1,taskUserMap2);
            //3.设置任务状态名称
            perfTaskvo.setTaskStatusName(setTaskStatusName(perfTaskvo.getTaskStatus()));
        }
        return taskInfoRespVOs;
    }

    @Override
    public PerformanceTaskListRespVO queryContractPerfTaskListById(ContractPerfPageReqVO contractPerfPageReqVO) {
        PerformanceTaskListRespVO performanceTaskListRespVO = new PerformanceTaskListRespVO();
        //1.查询合同信息
        ContractPerformanceDO contractPerformanceDO = contractPerforMapper.selectContractPerforById(contractPerfPageReqVO.getId());
        if (BeanUtil.isNotEmpty(contractPerformanceDO)) {
            performanceTaskListRespVO.setContractPerfId(contractPerformanceDO.getId());
            performanceTaskListRespVO.setContractName(contractPerformanceDO.getContractName());
            performanceTaskListRespVO.setContractCode(contractPerformanceDO.getContractCode());
            performanceTaskListRespVO.setContractStatus(contractPerformanceDO.getContractStatus());
            performanceTaskListRespVO.setContractStatusName(contractPerfService.setContractStatusName(performanceTaskListRespVO.getContractStatus()));
        }

        //2.查询履约任务信息
        PageResult<PerfTaskDO> perfTaskDOS = perforTaskMapper.queryContractPerfTaskList(contractPerfPageReqVO, null, null, "asc");
        PageResult<PerfTaskAndContractListRespVO> voList = perfTaskConverter.toVOList(perfTaskDOS);
        //3.查询所有任务类型,设置任务名称

        List<String> perfTaskTypeIds =  voList.getList().stream().map(PerfTaskAndContractListRespVO::getPerfTaskTypeId).collect(Collectors.toList());
        Map<String, PerformTaskTypeDO> perfTaskTypeMap = getPerformTaskTypeDOMap(perfTaskTypeIds);
        //4.查询用户
        Map<Long, AdminUserRespDTO> userRespDTOMap = getUserMap(voList.getList());
        int count = 0;
        for (PerfTaskAndContractListRespVO respVO : voList.getList()) {
            //设置任务名称
            if (StringUtil.isNotEmpty(respVO.getPerfTaskTypeId())) {
                respVO.setPerfTaskTypeName(perfTaskTypeMap.get(respVO.getPerfTaskTypeId()).getName());
            }
            //设置创建人
            if (StringUtil.isNotEmpty(respVO.getCreator())) {
                respVO.setCreatorName(userRespDTOMap.get(Long.valueOf(respVO.getCreator())).getNickname());
            }
            //设置确认人
            if (StringUtil.isNotEmpty(respVO.getConfirmer())) {
                respVO.setConfirmerName(userRespDTOMap.get(Long.valueOf(respVO.getConfirmer())).getNickname());
            }
            if (PerfTaskEnums.PERFORMANCE_FINISH.getCode().equals(respVO.getTaskStatus())
                    || PerfTaskEnums.OVER_TIME_FINISH.getCode().equals(respVO.getTaskStatus())) {
                count++;
            }
            //3.设置任务状态名称
            respVO.setTaskStatusName(setTaskStatusName(respVO.getTaskStatus()));
        }
        //5.设置任务列表数据
        performanceTaskListRespVO.setPerfTaskAndContractListRespVOList(voList.getList());
        //6.设置履约任务完成情况
        Long total = voList.getTotal();
        performanceTaskListRespVO.setFinishInfo(count + "/" + total);
        return performanceTaskListRespVO;
    }

    @Override
    public PageResult<PerfTaskAndContractListRespVO> queryContractPerfTaskList(ContractPerfPageReqVO contractPerfPageReqVO) {
        //1.查询履约任务信息
        PageResult<PerfTaskDO> perfTaskDOS;
        //点击履约监控无条件查一周
        if ("week".equals(contractPerfPageReqVO.getFlag()) && ObjectUtil.isEmpty(contractPerfPageReqVO.getStartTime()) && ObjectUtil.isEmpty(contractPerfPageReqVO.getEndTime())) {
            LocalDate firstDayOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
            // 获取本周最后一天
            LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(6);
            perfTaskDOS = perforTaskMapper.queryContractPerfTaskList(contractPerfPageReqVO, firstDayOfWeek, lastDayOfWeek, "desc");
        } else {
            perfTaskDOS = perforTaskMapper.queryContractPerfTaskList(contractPerfPageReqVO, null, null, "desc");
        }
        PageResult<PerfTaskAndContractListRespVO> voList = perfTaskConverter.toVOList(perfTaskDOS);
        //2.查询所有任务类型,设置任务名称
        List<String> perfTaskTypeIds =  voList.getList().stream().map(PerfTaskAndContractListRespVO::getPerfTaskTypeId).collect(Collectors.toList());
        Map<String, PerformTaskTypeDO> perfTaskTypeMap = getPerformTaskTypeDOMap(perfTaskTypeIds);
        //3.查询用户
        Map<Long, AdminUserRespDTO> userRespDTOMap = getUserMap(voList.getList());
        //4.查询履约合同信息
        List<String> contractPerfIds = voList.getList().stream().map(PerfTaskAndContractListRespVO::getContractPerfId).collect(Collectors.toList());
        List<ContractPerformanceDO> contractPerformanceDOS = contractPerforMapper.selectListByIds(contractPerfIds);
        Map<String, ContractPerformanceDO> contractPerfMap = CollectionUtils.convertMap(contractPerformanceDOS, ContractPerformanceDO::getId);

        for (PerfTaskAndContractListRespVO respVO : voList.getList()) {
            //设置任务名称
            respVO.setPerfTaskTypeName(perfTaskTypeMap.get(respVO.getPerfTaskTypeId()) == null ? null : perfTaskTypeMap.get(respVO.getPerfTaskTypeId()).getName());
            //设置创建人
            respVO.setCreatorName(userRespDTOMap.get(Long.valueOf(respVO.getCreator())).getNickname());
            //设置履约合同信息
            ContractPerformanceDO contractPerformanceDO = contractPerfMap.get(respVO.getContractPerfId());
            if (BeanUtil.isNotEmpty(contractPerformanceDO)) {
                respVO.setContractName(contractPerformanceDO.getContractName());
                respVO.setContractCode(contractPerformanceDO.getContractCode());
            }
            if (ObjectUtil.isNotEmpty(respVO.getConfirmer())) {
                //设置确认人名称
                respVO.setConfirmerName(userRespDTOMap.get(Long.valueOf(respVO.getConfirmer())).getNickname());
            }
            //3.设置任务状态名称
            respVO.setTaskStatusName(setTaskStatusName(respVO.getTaskStatus()));
        }
        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void surePerformance(PerfTaskReqVO perfTaskReqVO) throws IOException {
        PerfTaskDO entity = perfTaskConverter.toVO(perfTaskReqVO);
        //1.校验要修改的状态是否合理
        String perfId = checkTaskStatus(entity.getId(), entity.getTaskStatus());
        //2.设置确认时间
        entity.setConfirmTime(new Date());
        //3.设置确认人
        entity.setConfirmer(WebFrameworkUtils.getLoginUserId());
        //4.上传文件到minion
        if(ObjectUtil.isNotEmpty(perfTaskReqVO.getFile())){
            MultipartFile file = perfTaskReqVO.getFile();
            String source = "perfTask" + File.separator + DateUtil.today() + File.separator + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            long id = fileApi.uploadFile(file.getOriginalFilename(), source, IoUtil.readBytes(file.getInputStream()));
            //5.保存文件存放在ninio的路径
            entity.setFileId(id);
        }

        //6.修改履约任务状态
        perforTaskMapper.updateById(entity);
        //7.任务状态为履约完成或超期完成，修改下一个任务状态为履约中
        if (PerfTaskEnums.PERFORMANCE_FINISH.getCode().equals(entity.getTaskStatus())
                || PerfTaskEnums.OVER_TIME_FINISH.getCode().equals(entity.getTaskStatus())) {
            //7.1.查询下一个待履约状态的履约任务
            PerfTaskDO perfTaskDO = perforTaskMapper.queryLastPerfTask(perfId, PerfTaskEnums.WAIT_PERFORMANCE.getCode(), "asc", null);
            //不为空说明履约完成的任务不是最后一个
            if (BeanUtil.isNotEmpty(perfTaskDO)) {
                //1.2 设置履约任务状态
                if (! DateUtil.beginOfDay(new Date()).isAfter(perfTaskDO.getPerfTime())) {
                    //没过期，设置任务状态为履约中
                    perfTaskDO.setTaskStatus(PerfTaskEnums.IN_PERFORMANCE.getCode());
                } else {
                    //已过期，设置任务状态为履约超期
                    perfTaskDO.setTaskStatus(PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode());
                }
                //5.2.修改下一个履约任务状态
                perforTaskMapper.updateById(perfTaskDO);
                //6.修改合同状态
                contractPerforMapper.updateById(new ContractPerformanceDO().setId(perfId).setContractStatus(perfTaskDO.getTaskStatus()));
            } else {
                //8.完成的任务为最后一个，修改合同状态
                contractPerforMapper.updateById(new ContractPerformanceDO().setId(perfId).setContractStatus(entity.getTaskStatus()));
            }
        } else {
            //8.完成的任务为最后一个，修改合同状态
            contractPerforMapper.updateById(new ContractPerformanceDO().setId(perfId).setContractStatus(entity.getTaskStatus()));
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void surePerformanceV2(PerfTaskReqVO perfTaskReqVO) {
        PerfTaskDO entity = perfTaskConverter.toVO(perfTaskReqVO);
        //1.校验要修改的状态是否合理
        String perfId = checkTaskStatus(entity.getId(), entity.getTaskStatus());
        //2.设置确认时间
        entity.setConfirmTime(new Date());
        //3.设置确认人
        entity.setConfirmer(WebFrameworkUtils.getLoginUserId());
        //4.修改履约任务状态
        perforTaskMapper.updateById(entity);
        //5.任务状态为履约完成或超期完成，修改下一个任务状态为履约中
        if (PerfTaskEnums.PERFORMANCE_FINISH.getCode().equals(entity.getTaskStatus())
                || PerfTaskEnums.OVER_TIME_FINISH.getCode().equals(entity.getTaskStatus())) {
            //5.1.查询下一个待履约状态的履约任务
            PerfTaskDO perfTaskDO = perforTaskMapper.queryLastPerfTask(perfId, PerfTaskEnums.WAIT_PERFORMANCE.getCode(), "asc", null);
            //不为空说明履约完成的任务不是最后一个
            if (BeanUtil.isNotEmpty(perfTaskDO)) {
                //1.2 设置履约任务状态
                if (! DateUtil.beginOfDay(new Date()).isAfter(perfTaskDO.getPerfTime())) {
                    //没过期，设置任务状态为履约中
                    perfTaskDO.setTaskStatus(PerfTaskEnums.IN_PERFORMANCE.getCode());
                } else {
                    //已过期，设置任务状态为履约超期
                    perfTaskDO.setTaskStatus(PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode());
                }
                //5.2.修改下一个履约任务状态
                perforTaskMapper.updateById(perfTaskDO);
                //6.修改合同状态
                contractPerforMapper.updateById(new ContractPerformanceDO().setId(perfId).setContractStatus(perfTaskDO.getTaskStatus()));

            } else {
                //7.完成的任务为最后一个，修改合同状态
                contractPerforMapper.updateById(new ContractPerformanceDO().setId(perfId).setContractStatus(entity.getTaskStatus()));
            }
        } else {
            //8.完成的任务为最后一个，修改合同状态
            contractPerforMapper.updateById(new ContractPerformanceDO().setId(perfId).setContractStatus(entity.getTaskStatus()));
        }
    }
    @Override
    public List<PerfTaskAndContractListRespVO> queryPerfRemindTaskList() {
        List<PerfTaskDO> perfTaskDOS = perforTaskMapper.selectList(new LambdaQueryWrapperX<PerfTaskDO>()
                .eqIfPresent(PerfTaskDO::getRemindTime, LocalDate.now()));
        List<PerfTaskAndContractListRespVO> voList = perfTaskConverter.toVOList(perfTaskDOS);
        //3.查询所有任务类型,设置任务名称
        List<String> perfTaskTypeIds = voList.stream().map(PerfTaskAndContractListRespVO::getPerfTaskTypeId).collect(Collectors.toList());
        Map<String, PerformTaskTypeDO> perfTaskTypeMap = getPerformTaskTypeDOMap(perfTaskTypeIds);
        //4.查询用户
        Map<Long, AdminUserRespDTO> userRespDTOMap = getUserMap(voList);
        for (PerfTaskAndContractListRespVO respVO : voList) {
            //设置任务类型名称
            respVO.setPerfTaskTypeName(perfTaskTypeMap.get(respVO.getPerfTaskTypeId()).getName());
            //设置创建人
            respVO.setCreatorName(userRespDTOMap.get(Long.valueOf(respVO.getCreator())).getNickname());
            //3.设置任务状态名称
            respVO.setTaskStatusName(setTaskStatusName(respVO.getTaskStatus()));
        }
        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void continuePerformance(String contractPerfId) {
        PerfTaskDO perfTaskDO;
        //1.修改履约任务状态
        //1.1 根据合同履约id查询任务状态为履约暂停得到任务信息
        perfTaskDO = perforTaskMapper.queryPerfTime(contractPerfId, PerfTaskEnums.PERFORMANCE_PAUSE.getCode());
        if (BeanUtil.isNotEmpty(perfTaskDO)) {
            //1.2 设置履约任务状态
            if (! DateUtil.beginOfDay(new Date()).isAfter(perfTaskDO.getPerfTime())) {
                //没过期，设置任务状态为履约中
                perfTaskDO.setTaskStatus(PerfTaskEnums.IN_PERFORMANCE.getCode());
            } else {
                //已过期，设置任务状态为履约超期
                perfTaskDO.setTaskStatus(PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode());
            }

        }else{
            //为超期暂停
            //1.1 根据合同履约id查询任务状态为超期暂停得到任务信息
            perfTaskDO = perforTaskMapper.queryPerfTime(contractPerfId, PerfTaskEnums.OVER_TIME_PAUSE.getCode());
            perfTaskDO.setTaskStatus(PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode());
        }
        //1.3 修改履约任务状态
        perforTaskMapper.updateById(perfTaskDO);
        //2.修改履约合同状态
        contractPerforMapper.updateById(new ContractPerformanceDO().setId(contractPerfId).setContractStatus(perfTaskDO.getTaskStatus()));
    }
    @DataPermission(enable = false)
    private Map<Long, AdminUserRespDTO> getTaskUserMap1(List<PerfTaskUserDO> perfTaskUserDOS){
        List<Long> userIds = perfTaskUserDOS.stream().map(PerfTaskUserDO::getUserId).collect(Collectors.toList());
        List<AdminUserRespDTO> userList = adminUserApi.getUserList(userIds);
        Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(userList, AdminUserRespDTO::getId);
        return userMap;
    }

    private Map<String, List<Long>> getTaskUserMap2(List<PerfTaskUserDO> perfTaskUserDOS){
        Map<String, List<Long>> mapS = perfTaskUserDOS.stream()
                .collect(Collectors.groupingBy(PerfTaskUserDO::getPerfTaskId,
                        Collectors.mapping(PerfTaskUserDO::getUserId, Collectors.toList())));
        return mapS;
    }


    private void setPerfStatus(PerfTaskDO entry) {
        PerfTaskDO perfTaskDO = null;
        PerfTaskDO perfTaskDO1 = perforTaskMapper.selectById(entry.getId());
        if (BeanUtil.isNotEmpty(perfTaskDO1) && PerfTaskEnums.IN_PERFORMANCE.getCode().equals(perfTaskDO1.getTaskStatus())) {
            //編輯履约中的任务
            //1.查询待履约任务中履约时间最早的
            perfTaskDO = perforTaskMapper.queryLastPerfTask(entry.getContractPerfId(), PerfTaskEnums.WAIT_PERFORMANCE.getCode(), "asc", null);
        } else {
            //2.新增或者是编辑待履约任务
            perfTaskDO = perforTaskMapper.queryLastPerfTask(entry.getContractPerfId(), PerfTaskEnums.IN_PERFORMANCE.getCode(), null, entry.getId());

            if(BeanUtil.isEmpty(perfTaskDO)){
                perfTaskDO = perforTaskMapper.queryLastPerfTask(entry.getContractPerfId(), PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode(), null, entry.getId());
            }
        }
        // 比较两个日期,前<后-><0 前>后->>0
        if (ObjectUtil.isNotEmpty(perfTaskDO)) {
           if(PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode().equals(perfTaskDO.getTaskStatus())){
               entry.setTaskStatus(PerfTaskEnums.WAIT_PERFORMANCE.getCode());
           }else {
               if (perfTaskDO.getPerfTime().before(entry.getPerfTime())) {
                   //原来履约中的任务的履约时间大于新增任务的履约时间-》新增履约任务状态为待履约
                   entry.setTaskStatus(PerfTaskEnums.WAIT_PERFORMANCE.getCode());
                   perfTaskDO.setTaskStatus(PerfTaskEnums.IN_PERFORMANCE.getCode());
               } else if (perfTaskDO.getPerfTime().after(entry.getPerfTime())) {
                   //原来履约中的任务的履约时间小于新增任务的履约时间-》新增履约任务状态为履约中，原来履约中的状态改为待履约
                   //1.新增履约任务状态设置为履约中
                   entry.setTaskStatus(PerfTaskEnums.IN_PERFORMANCE.getCode());
                   //2.原来履约中的状态改为待履约
                   perfTaskDO.setTaskStatus(PerfTaskEnums.WAIT_PERFORMANCE.getCode());
               } else {
                   //两个日期相同  谁的创建时间在前谁为履约中
                   entry.setTaskStatus(PerfTaskEnums.WAIT_PERFORMANCE.getCode());
               }
               perforTaskMapper.updateById(perfTaskDO);
           }
        } else {
            //为空说明为第一个履约任务
            entry.setTaskStatus(PerfTaskEnums.IN_PERFORMANCE.getCode());
        }

    }


    private Date isBigTime(PerfTaskDO entry) {
        Date bigTime = entry.getPerfTime();
        PerfTaskDO perfTaskDO = perforTaskMapper.queryLastPerfTask(entry.getContractPerfId(), null, "desc", null);
        //不等于空说明不是第一个任务
        if (BeanUtil.isNotEmpty(perfTaskDO)) {
            if (perfTaskDO.getPerfTime().after(bigTime)) {
                //新履约时间小于原来最终履约时间
                bigTime = perfTaskDO.getPerfTime();
            }
        }
        return bigTime;
    }

    /**
     * 校验履约任务是否可删除，编辑
     */
    private void isDeleteOrUpdate(String id) {
        PerfTaskDO perfTaskDO = perforTaskMapper.selectById(id);
        if (BeanUtil.isNotEmpty(perfTaskDO)) {
            switch (PerfTaskEnums.getInstance(perfTaskDO.getTaskStatus())) {
                case PERFORMANCE_FINISH:
                    throw exception(ErrorCodeConstants.NOT_DEL_ADN_UPDATE);
                case PERFORMANCE_PAUSE:
                    throw exception(ErrorCodeConstants.NOT_DEL_ADN_UPDATE);
                case PERFORMANCE_END:
                    throw exception(ErrorCodeConstants.NOT_DEL_ADN_UPDATE);
                case PERFORMANCE_OVER_TIME:
                    throw exception(ErrorCodeConstants.NOT_DEL_ADN_UPDATE);
                case OVER_TIME_PAUSE:
                    throw exception(ErrorCodeConstants.NOT_DEL_ADN_UPDATE);
                case OVER_TIME_END:
                    throw exception(ErrorCodeConstants.NOT_DEL_ADN_UPDATE);
                case OVER_TIME_FINISH:
                    throw exception(ErrorCodeConstants.NOT_DEL_ADN_UPDATE);
            }
        }

    }
    @DataPermission(enable = false)
    private String getUserName(Long id) {
        //5.设置创建人名称
        AdminUserRespDTO user = adminUserApi.getUser(id);
        return user.getNickname();
    }

    private void setPerformanceTaskInfoRespVO(PerformanceTaskInfoRespVO taskInfoRespVO,Map<String, PerformTaskTypeDO> taskTypeDOMap,Map<String, PerfTaskDO> perfTaskDOMap,Map<Long, AdminUserRespDTO> taskUserMap1,Map<String, List<Long>> mapS ) {
        //2.设置履约任务类型名称
        if (StringUtil.isNotEmpty(taskInfoRespVO.getPerfTaskTypeId())) {
        if(ObjectUtil.isEmpty(taskTypeDOMap)){
            //为单个任务时
                PerformTaskTypeDO performTaskTypeDO = taskTypeMapper.selectById(taskInfoRespVO.getPerfTaskTypeId());
                taskInfoRespVO.setPerfTaskTypeName(performTaskTypeDO == null ? null : performTaskTypeDO.getName());
        }else{
            taskInfoRespVO.setPerfTaskTypeName(taskTypeDOMap.get(taskInfoRespVO.getPerfTaskTypeId()).getName());
        }
        }
        //3.设置前置履约任务名称
        if (StringUtil.isNotEmpty(taskInfoRespVO.getBeforTaskId())) {
            if(ObjectUtil.isEmpty(perfTaskDOMap)){
                PerfTaskDO perfTaskDO1 = perforTaskMapper.selectById(taskInfoRespVO.getBeforTaskId());
                taskInfoRespVO.setBeforTaskName(perfTaskDO1 == null ? null : perfTaskDO1.getName());
            }else {
                taskInfoRespVO.setBeforTaskName(perfTaskDOMap.get(taskInfoRespVO.getBeforTaskId())==null?null:perfTaskDOMap.get(taskInfoRespVO.getBeforTaskId()).getName());
            }

        }
        //4.设置用户信息
            List<Map<String, Object>> users = new ArrayList<>();
        List<Long> userids = mapS.get(taskInfoRespVO.getId());
        if(CollUtil.isNotEmpty(userids)){
            for (Long userid : userids) {
                Map<String, Object> user = new HashMap<>();
                user.put("userId",userid);
                user.put("userName",taskUserMap1.get(userid)==null?null:taskUserMap1.get(userid).getNickname());
                users.add(user);
            }
        }
            taskInfoRespVO.setUserInfo(users);
        //5.设置创建人名称
        taskInfoRespVO.setCreatorName(getUserName(Long.valueOf(taskInfoRespVO.getCreator())));
        //6.设置确认人名称
        if (ObjectUtil.isNotEmpty(taskInfoRespVO.getConfirmer())) {
            taskInfoRespVO.setConfirmerName(getUserName(Long.valueOf(taskInfoRespVO.getConfirmer())));
        }
    }

    private Map<String, PerformTaskTypeDO> getPerformTaskTypeDOMap( List<String> perfTaskTypeIds) {
        //2.查询所有任务类型,设置任务名称
        List<PerformTaskTypeDO> performTaskTypeDOS = taskTypeMapper.selectListByIds( perfTaskTypeIds);
        Map<String, PerformTaskTypeDO> perfTaskTypeMap = CollectionUtils.convertMap(performTaskTypeDOS, PerformTaskTypeDO::getId);
        return perfTaskTypeMap;
    }
    private Map<String, PerfTaskDO> getPerfTaskDOMap(String perfContractId) {
        List<PerfTaskDO> perfTaskDOS = perforTaskMapper.selectListById(perfContractId);
        Map<String, PerfTaskDO> perfTaskMap = CollectionUtils.convertMap(perfTaskDOS, PerfTaskDO::getId);
        return perfTaskMap;
    }
    @DataPermission(enable = false)
    private Map<Long, AdminUserRespDTO> getUserMap(List<PerfTaskAndContractListRespVO> voList) {
        //4.查询用户
        List<String> creatorIds = voList.stream().map(PerfTaskAndContractListRespVO::getCreator).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<String> confirmerIds = voList.stream().map(PerfTaskAndContractListRespVO::getConfirmer).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<String> mergedList = Stream.concat(creatorIds.stream(), confirmerIds.stream()).distinct().collect(Collectors.toList());
        List<Long> ids = mergedList.stream().map(Long::valueOf).collect(Collectors.toList());
        List<AdminUserRespDTO> userList = adminUserApi.getUserList(ids);
        Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(userList, AdminUserRespDTO::getId);
        return userRespDTOMap;
    }

    private String checkTaskStatus(String id, Integer status) {
        PerfTaskDO perfTaskDO = perforTaskMapper.selectById(id);
        //若为履约中，只可以改为履约暂停，履约结束，履约完成
        if (ContractPerfEnums.IN_PERFORMANCE.getCode().equals(perfTaskDO.getTaskStatus())) {
            if (!ContractPerfEnums.PERFORMANCE_PAUSE.getCode().equals(status)
                    && !ContractPerfEnums.PERFORMANCE_END.getCode().equals(status)
                    && !ContractPerfEnums.PERFORMANCE_FINISH.getCode().equals(status)) {
                throw exception(ErrorCodeConstants.IN_PERFORMANCE_ERROR);
            }
            //若为履约暂停，只可以改为履约中，履约结束，履约完成
        } else if (ContractPerfEnums.PERFORMANCE_PAUSE.getCode().equals(perfTaskDO.getTaskStatus())) {
            if (!ContractPerfEnums.IN_PERFORMANCE.getCode().equals(status)
                    && !ContractPerfEnums.PERFORMANCE_END.getCode().equals(status)
                    && !ContractPerfEnums.PERFORMANCE_FINISH.getCode().equals(status)) {
                throw exception(ErrorCodeConstants.PERFORMANCE_PAUSE_ERROR);
            }
            //若为履约超期，只可以改为超期暂停，超期结束，超期完成
        } else if (ContractPerfEnums.PERFORMANCE_OVER_TIME.getCode().equals(perfTaskDO.getTaskStatus())) {
            if (!ContractPerfEnums.OVER_TIME_PAUSE.getCode().equals(status)
                    && !ContractPerfEnums.OVER_TIME_END.getCode().equals(status)
                    && !ContractPerfEnums.OVER_TIME_FINISH.getCode().equals(status)) {
                throw exception(ErrorCodeConstants.PERFORMANCE_OVER_TIME_ERROR);
            }
            //若为超期暂停，只可以改为履约超期，超期结束，超期完成
        } else if (ContractPerfEnums.OVER_TIME_PAUSE.getCode().equals(perfTaskDO.getTaskStatus())) {
            if (!ContractPerfEnums.PERFORMANCE_OVER_TIME.getCode().equals(status)
                    && !ContractPerfEnums.OVER_TIME_END.getCode().equals(status)
                    && !ContractPerfEnums.OVER_TIME_FINISH.getCode().equals(status)) {
                throw exception(ErrorCodeConstants.OVER_TIME_PAUSE_ERROR);
            }
        } else {
            if (ObjectUtil.isNotEmpty(perfTaskDO.getTaskStatus())) {
                throw exception(ErrorCodeConstants.SURE_PERF_ERROR);
            }
        }
        return perfTaskDO.getContractPerfId();
    }

    public String setTaskStatusName(Integer status) {
        String name;
        switch (PerfTaskEnums.getInstance(status)) {
            case PERFTASK_NO_START:
                name = PerfTaskEnums.PERFTASK_NO_START.getDesc();
                break;
            case WAIT_PERFORMANCE:
                name = PerfTaskEnums.WAIT_PERFORMANCE.getDesc();
                break;
            case IN_PERFORMANCE:
                name = PerfTaskEnums.IN_PERFORMANCE.getDesc();
                break;
            case PERFORMANCE_FINISH:
                name = PerfTaskEnums.PERFORMANCE_FINISH.getDesc();
                break;
            case PERFORMANCE_PAUSE:
                name = PerfTaskEnums.PERFORMANCE_PAUSE.getDesc();
                break;
            case PERFORMANCE_END:
                name = PerfTaskEnums.PERFORMANCE_END.getDesc();
                break;
            case PERFORMANCE_OVER_TIME:
                name = PerfTaskEnums.PERFORMANCE_OVER_TIME.getDesc();
                break;
            case OVER_TIME_PAUSE:
                name = PerfTaskEnums.OVER_TIME_PAUSE.getDesc();
                break;
            case OVER_TIME_END:
                name = PerfTaskEnums.OVER_TIME_END.getDesc();
                break;
            case OVER_TIME_FINISH:
                name = PerfTaskEnums.OVER_TIME_FINISH.getDesc();
                break;
            default:
                throw new IllegalArgumentException("履约任务状态码不正确");
        }
        return name;

    }
}
