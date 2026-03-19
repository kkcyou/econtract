package com.yaoan.module.econtract.service.bpm.register;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelBpmSubmitCreateReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.register.vo.BigContractRegisterListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.register.vo.ContractRegisterListApproveReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.register.vo.ContractRegisterListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.SignatoryRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.convert.config.SystemConfigDTOConverter;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.register.BpmContractRegisterDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.mysql.bpm.register.BpmContractRegisterMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.SimpleContractMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.StatusConstants;
import com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.flowable.FlowableUtil;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.dto.UserCompanyInfoRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_DATA_ERROR;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/26 10:58
 */
@Service
public class ContractRegisterBpmServiceImpl implements ContractRegisterBpmService {
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private SimpleContractMapper simpleContractMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private BpmContractRegisterMapper bpmContractRegisterMapper;
    @Resource
    private SystemConfigApi systemConfigApi;

    @Override
    public String submit(Long loginUserId, IdReqVO reqVO) {
        //判断走审批流
        if (systemConfigApi.ifApprove(ActivityConfigurationEnum.MODEL_APPROVE)) {
            // 走审批流
            return submitOne(loginUserId, reqVO);
        } else {
            // 不走审批流，直接通过
            return fastPassOne(reqVO);
        }
    }

    public String submitOne(Long loginUserId, IdReqVO reqVO) {

        String contractId = reqVO.getId();
        ContractDO contractDO = contractMapper.selectById(contractId);


        if (ObjectUtil.isNull(contractDO)) {
            throw exception(EMPTY_DATA_ERROR);
        }
        //1.插入请求单
        //校验是否发起过审批
        ContractStatusEnums statusEnums = ContractStatusEnums.getInstance(contractDO.getStatus());
        if (ContractStatusEnums.TO_BE_CHECK != statusEnums) {
            throw exception(ErrorCodeConstants.CONTRACT_BPM_EXISTS);
        }
        BpmContractRegisterDO bpmDO = new BpmContractRegisterDO().setContractId(contractDO.getId())
                .setContractName(contractDO.getName())
                .setReason(contractDO.getChangeReason())
                .setUserId(loginUserId)
                .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        // 2 发起申请 BPM
        // 2.1 流程变量
        Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
        processInstanceVariables.put("contractId", contractId);
        //获取流程key
        String key = ActivityConfigurationEnum.CONTRACT_REGISTER_APPLICATION_APPROVE.getDefinitionKey();
        if(ObjectUtil.isNotEmpty(contractDO.getContractType())){
            ContractType contractType = contractTypeMapper.selectById(contractDO.getContractType());
            if(ObjectUtil.isNotEmpty(contractType) && ObjectUtil.isNotEmpty(contractType.getRegisterProcess())){
                key = contractType.getRegisterProcess();
            }
        }
        // 2.2 流程实例id
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO()
                        .setProcessDefinitionKey(key)
                        .setVariables(processInstanceVariables).setBusinessKey(contractId));
        bpmContractRegisterMapper.insert(bpmDO.setProcessInstanceId(processInstanceId));
        //合同变成审批中
        contractDO.setStatus(ContractStatusEnums.CHECKING.getCode());
        contractDO.setProcessInstanceId(processInstanceId);
        contractMapper.updateById(contractDO);
        return bpmDO.getId();
    }

    private String fastPassOne(IdReqVO reqVO) {
        BpmContractRegisterDO bpmDO = new BpmContractRegisterDO().setId(IdUtil.simpleUUID())
                //绑定合同
                .setContractId(reqVO.getId())
                //直接自动通过
                .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());
        bpmContractRegisterMapper.insert(bpmDO);
        return "success";
    }

    @Override
    public String submitBatch(Long loginUserId, IdReqVO vo) {
        return null;
    }


    @Override
    public BigContractRegisterListApproveRespVO getBpmAllTaskPage(Long loginUserId, ContractRegisterListApproveReqVO pageVO) {
        //查询合同类型表，取起草流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .eq(ContractType::getTypeStatus,1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getRegisterProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        // 查询所有任务
        if(ObjectUtil.isNotEmpty(definitionKeys)){
             processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(definitionKeys.get(0),definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
        }else {
             processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_REGISTER_APPLICATION_APPROVE.getDefinitionKey());
        }

//        processInstanceRelationInfoRespDTOList = EcontractUtil.clearRepealTask(processInstanceRelationInfoRespDTOList);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<SimpleContractDO> doPageResult = simpleContractMapper.selectContractRegisterApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public BigContractRegisterListApproveRespVO getBpmDoneTaskPage(Long loginUserId, ContractRegisterListApproveReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        // 查询待办任务
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        //查询合同类型表，取起草流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .eq(ContractType::getTypeStatus,1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getRegisterProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        //获得已处理任务数据(已过滤掉已取消的任务),可筛选审批状态
        if(ObjectUtil.isNotEmpty(definitionKeys)){
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(definitionKeys.get(0), taskResult,definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));

        }else{
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.CONTRACT_REGISTER_APPLICATION_APPROVE.getDefinitionKey(), taskResult);
        }
        //去除已取消的任务。
//        processInstanceRelationInfoRespDTOList = EcontractUtil.clearRepealTask(processInstanceRelationInfoRespDTOList);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<SimpleContractDO> doPageResult = simpleContractMapper.selectContractRegisterApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public BigContractRegisterListApproveRespVO getBpmToDoTaskPage(Long loginUserId, ContractRegisterListApproveReqVO pageVO) {
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .eq(ContractType::getTypeStatus,1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getRegisterProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        // 查询待办任务
        if(ObjectUtil.isNotEmpty(definitionKeys)){
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(definitionKeys.get(0),definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
        }else{
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_REGISTER_APPLICATION_APPROVE.getDefinitionKey());
        }

        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<SimpleContractDO> doPageResult = simpleContractMapper.selectContractRegisterApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    private BigContractRegisterListApproveRespVO enhanceBpmPage(PageResult<SimpleContractDO> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {

        List<SimpleContractDO> doList = doPageResult.getList();
        List<ContractRegisterListApproveRespVO> respVOList = ContractConverter.INSTANCE.convertRegisterBpmDO2RespList2(doList);
        if (CollectionUtil.isNotEmpty(doList)) {
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            //合同类型map
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            List<String> doIdList = doList.stream().map(SimpleContractDO::getId).collect(Collectors.toList());
            List<BpmContractRegisterDO> bpmDOList = bpmContractRegisterMapper.selectList(new LambdaQueryWrapperX<BpmContractRegisterDO>()
                    .inIfPresent(BpmContractRegisterDO::getContractId, doIdList));
            Map<String, BpmContractRegisterDO> bpmDOMap = CollectionUtils.convertMap(bpmDOList, BpmContractRegisterDO::getContractId);
            //发起人信息map
            List<AdminUserRespDTO> userRespDTOList = adminUserApi.getUserList();
            Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);

            //签署方信息准备阶段
            List<String> contractIds = doIdList;
            // 获取签署方ids
            List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(new LambdaQueryWrapperX<SignatoryRelDO>().inIfPresent(SignatoryRelDO::getContractId, contractIds));
            List<String> signatoryIds = signatoryRelDOS.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
            Map<String,SignatoryRelDO> signatoryRelMap = CollectionUtils.convertMap(signatoryRelDOS, SignatoryRelDO::getContractId);
            //获取相对方信息
            List<Relative> relativeList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(signatoryIds)) {
                relativeList = relativeMapper.selectBatchIds(signatoryIds);
            }

            // 获取签署方信息-个人
            List<String> creatorIdsStr = doList.stream().map(SimpleContractDO::getCreator).collect(Collectors.toList());
            List<Long> creatorIds = creatorIdsStr.stream().map(Long::valueOf).collect(Collectors.toList());
            //根据创建人ids获取公司信息
            List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(creatorIds);
            Map<Long, UserCompanyInfoRespDTO> userCompanyMap = CollectionUtils.convertMap(userCompanyInfoList, UserCompanyInfoRespDTO::getUserId);

            // 发起方是个体，通过id 获取用户 nickname
            List<AdminUserRespDTO> users = adminUserApi.getUserList(userCompanyInfoList.stream().map(UserCompanyInfoRespDTO::getUserId).collect(Collectors.toList()));
            Map<Long, AdminUserRespDTO> userListByDeptMap = CollectionUtils.convertMap(users, AdminUserRespDTO::getId);

            //合同
            List<String> contractIdList = doList.stream().map(SimpleContractDO::getId).collect(Collectors.toList());
            List<SimpleContractDO> contractDOList = simpleContractMapper.selectList(new LambdaQueryWrapperX<SimpleContractDO>().in(SimpleContractDO::getId, contractIdList));
            Map<String, SimpleContractDO> contractDOMap = CollectionUtils.convertMap(contractDOList, SimpleContractDO::getId);

            //流程信息
            List<String> instanceList = bpmDOList.stream().map(BpmContractRegisterDO::getProcessInstanceId).collect(Collectors.toList());

            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> taskEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> taskDoneEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();

            Long loginUserId = getLoginUserId();
            List<BpmTaskAllInfoRespVO> originalTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            List<BpmTaskAllInfoRespVO> taskDoneEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            //待处理的任务
            Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();


            if (CollectionUtil.isNotEmpty(instanceList)) {
                originalTaskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(originalTaskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTimeByUserId(originalTaskAllInfoRespVOList, getLoginUserId());
                toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                taskDoneEndTimeAllInfoRespVOList = EcontractUtil.distinctDoneTaskLatestEndTime(originalTaskAllInfoRespVOList);
                taskDoneEndTimeMap = CollectionUtils.convertMap(taskDoneEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);


            }

            //有结束时间的流程任务
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskEndTimeAllInfoRespVOList = EcontractUtil.distinctTaskHaveEndTime(originalTaskAllInfoRespVOList);
                taskEndTimeMap = CollectionUtils.convertMap(taskEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }

            for (ContractRegisterListApproveRespVO respVO : respVOList) {
                //合同类型
                ContractType contractType = contractTypeMap.get(respVO.getContractType());
                if (ObjectUtil.isNotNull(contractType)) {
                    respVO.setContractTypeName(contractType.getName());
                }
                BpmContractRegisterDO bpmDO = bpmDOMap.get(respVO.getId());
                if (ObjectUtil.isNotNull(bpmDO)) {
                    respVO.setSubmitTime(bpmDO.getCreateTime());

                    //流程实例
                    respVO.setProcessInstanceId(bpmDO.getProcessInstanceId());

                    //流程任务
                    ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO = instanceRelationInfoRespDTOMap.get(bpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)) {
                        respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
                    }
                    List<String> signatoryNameList = signatoryNameById(relativeList, userCompanyMap, respVO.getId(), contractDOMap, userListByDeptMap, signatoryRelMap);
                    List<SignatoryRespVO> signatoryRespVOList = new ArrayList<>();
                    for (String s : signatoryNameList) {
                        signatoryRespVOList.add(new SignatoryRespVO().setSignatory(s));
                    }
                    respVO.setSignatoryRespVOList(signatoryRespVOList);
                }
                //审批状态的三个字段
                //历史任务信息
                BpmTaskAllInfoRespVO historyTask = taskEndTimeMap.get(bpmDO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(historyTask)) {
                    respVO.setHisTaskResult(historyTask.getResult());
                }
                //全部审批列表的审批状态(找不到当前登录人的待办任务，既是已办任务)
                BpmTaskAllInfoRespVO toDoTask = toDoTaskMap.get(bpmDO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(toDoTask)) {
                    respVO.setAssigneeId(toDoTask.getAssigneeUserId());
                    respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTask.getAssigneeUserId()));
                }
                //审批状态
                else {
                    respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
                }
                //已审批任务的状态赋值（当前人对该审批的最近的一次操作结果）
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = instanceRelationInfoRespDTOMap.get(bpmDO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setDoneTaskResult(infoRespDTO.getProcessResult());
                }
            }
            PageResult<ContractRegisterListApproveRespVO> pageResult = new PageResult<ContractRegisterListApproveRespVO>();
            pageResult.setList(respVOList).setTotal(doPageResult.getTotal());
            BigContractRegisterListApproveRespVO respVO = new BigContractRegisterListApproveRespVO().setPageResult(pageResult);
            //获取配置
            respVO.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_REGISTER_APPLICATION_APPROVE)));
            return respVO;
        }
        BigContractRegisterListApproveRespVO result = new BigContractRegisterListApproveRespVO()
                .setPageResult(new PageResult<ContractRegisterListApproveRespVO>()
                        .setList(Collections.emptyList())
                        .setTotal(doPageResult.getTotal())
                );
        result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.MODEL_APPROVE)));
        return result;
    }

    //根据合同id 获取签署方名称
    private List<String> signatoryNameById(List<Relative> relativeList, Map<Long, UserCompanyInfoRespDTO> userCompanyMap, String id,
                                           Map<String, SimpleContractDO> contractMap, Map<Long, AdminUserRespDTO> userListByDeptMap, Map<String,SignatoryRelDO> signatoryRelMap) {
        List<String> signatoryNameList = new ArrayList<>();
        SimpleContractDO contractDO = contractMap.get(id);
        if (ObjectUtil.isNotNull(contractDO)) {
            UserCompanyInfoRespDTO companyInfoRespDTO = userCompanyMap.get(Long.valueOf(contractDO.getCreator()));
            if (ObjectUtil.isNotNull(companyInfoRespDTO)) {
                signatoryNameList.add(companyInfoRespDTO.getName());

            } else {
                //发起方是个体，通过id 获取nickname
                AdminUserRespDTO userRespDTO = userListByDeptMap.get(contractDO.getCreator());
                if (ObjectUtil.isNotNull(userRespDTO)) {
                    signatoryNameList.add(userRespDTO.getNickname());
                }
            }

            for (Relative relative : relativeList) {
                SignatoryRelDO signatoryRelDO = signatoryRelMap.get(id);
                if(ObjectUtil.isNotEmpty(signatoryRelDO)){
                    if(signatoryRelDO.getSignatoryId().equals(relative.getId())){
                        signatoryNameList.add(relative.getName());
                    }
                }
//                if (!relative.getCompanyName().isEmpty()) {
//                    signatoryNameList.add(relative.getCompanyName());
//                } else {
//                    signatoryNameList.add(relative.getName());
//                }
            }
        }

        return signatoryNameList;
    }


}
