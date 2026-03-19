package com.yaoan.module.econtract.service.bpm.performance.suspend;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAssignRespDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskCreateDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskRespDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.performance.suspend.vo.BpmProcessRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.performance.suspend.vo.BpmSuspendCreateReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.performance.suspend.BpmPerformance;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.mysql.bpm.performance.suspend.BpmPerformanceMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.enums.ApproveTypeEnum;
import com.yaoan.module.econtract.enums.ContractPerfEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.WhetherEnum;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
@Service
public class BpmPerformanceSuspendServiceImpl implements BpmPerformanceSuspendService {

    @Resource
    private ContractPerforMapper contractPerforMapper;
    @Resource
    private BpmPerformanceMapper bpmPerformanceMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private AdminUserApi adminUserApi;

    public static final String PROCESS_KEY = "performance_suspend";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createSuspend(Long loginUserId, BpmSuspendCreateReqVO suspendCreateReqVO) {

        ContractPerformanceDO performanceDO = contractPerforMapper.selectById(suspendCreateReqVO.getId());

        if (performanceDO == null) {
            throw exception(ErrorCodeConstants.NO_DATA_FIND_ERROR);
        }
        performanceDO.setContractStatus(ContractPerfEnums.TERMINAT_APPROVE.getCode());
        contractPerforMapper.updateById(performanceDO);

        //1.插入请求单
        BpmPerformance bpmPerformance = new BpmPerformance().setPerformanceId(performanceDO.getId()).setApproveType(ApproveTypeEnum.SUSPEND.getCode())
                .setContractCode(performanceDO.getContractCode()).setContractName(performanceDO.getContractName())
                .setContractType(performanceDO.getContractTypeId()).setReason(suspendCreateReqVO.getReason())
                .setUserId(loginUserId).setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());

        bpmPerformanceMapper.insert(bpmPerformance);
        //2.发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("reason", suspendCreateReqVO.getReason());
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(bpmPerformance.getId()));

        //3.将工作流的编号，更新到申请单中
        bpmPerformanceMapper.updateById(new BpmPerformance().setId(bpmPerformance.getId()).setProcessInstanceId(processInstanceId));

        return bpmPerformance.getId();
    }
    /**
     * @param processInstanceId 流程实例ID
     * @return 审批进程
     */
    @Override
    public List<BpmProcessRespVO> process(String processInstanceId) {

        BpmTaskCreateDTO taskCreateDTO = bpmTaskApi.getCreateInfoByProcessInstanceId(processInstanceId);

//        BpmPerformance bpmPerformance = bpmPerformanceMapper.selectOne(BpmPerformance::getProcessInstanceId, processInstanceId);
        if (taskCreateDTO == null) {
            return Collections.emptyList();
        }
        List<BpmProcessRespVO> result = new ArrayList<>();
        List<BpmTaskAssignRespDTO> tasks = bpmTaskApi.getAllTaskNameByProcessInstanceId(processInstanceId);
        List<Long> taskUserIds = tasks.stream().map(BpmTaskAssignRespDTO::getUserId).collect(Collectors.toList());
        List<Long> userIds = new ArrayList<>();
        userIds.add(taskCreateDTO.getUserId());
        userIds.addAll(taskUserIds);

        List<AdminUserRespDTO> userList = adminUserApi.getUserList(userIds);
        Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(userList, AdminUserRespDTO::getId);

        AdminUserRespDTO startUser = userMap.get(taskCreateDTO.getUserId());

        BpmProcessRespVO startNode = new BpmProcessRespVO().setUserId(startUser == null ? taskCreateDTO.getUserId() : startUser.getId()).setUserName(startUser == null ? "" : startUser.getNickname()).setResult(BpmProcessInstanceResultEnum.APPROVE.getResult()).setTaskName("提交审批").setOperateTime(taskCreateDTO.getCreateTime());
        result.add(startNode);

        List<BpmTaskRespDTO> taskInfos = bpmTaskApi.getTaskInfoListByProcessInstanceId(processInstanceId);
        Map<String, BpmTaskRespDTO> keyMap = CollectionUtils.convertMap(taskInfos, BpmTaskRespDTO::getDefinitionKey);

        if (CollectionUtil.isNotEmpty(tasks)) {
            tasks.forEach(item -> {
                BpmProcessRespVO target = new BpmProcessRespVO();
                AdminUserRespDTO userInfo = userMap.get(item.getUserId());
                if (userInfo != null) {
                    target.setUserName(userInfo.getNickname());
                }
                target.setUserId(item.getUserId());
                BpmTaskRespDTO bpmTaskRespDTO = keyMap.get(item.getDefinitionKey());
                if (bpmTaskRespDTO != null && bpmTaskRespDTO.getResult() != null) {
                    target.setResult(bpmTaskRespDTO.getResult());
                    target.setOperateTime(bpmTaskRespDTO.getEndTime());
                    target.setTaskName(bpmTaskRespDTO.getDefinitionKey());
                    target.setUserName(bpmTaskRespDTO.getAssigneeUserName());
                }
                result.add(target);
            });
            BpmProcessRespVO bpmProcessRespVO = result.get(result.size() - 1);
            result.add(new BpmProcessRespVO().setResult(bpmProcessRespVO.getResult()).setOperateTime(bpmProcessRespVO.getOperateTime()).setTaskName("结束"));
        }
        return result;
    }
}
