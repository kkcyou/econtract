package com.yaoan.module.econtract.service.contractInvoiceManage;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.util.StringUtils;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.CodeQueryReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.BpmTaskApproveReqVO;
import com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo.ContractInvoiceManagePageReqVO;
import com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo.ContractInvoiceManageRespVO;
import com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo.ContractInvoiceManageSaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.PaymentPlanRespVO;
import com.yaoan.module.econtract.convert.payment.PaymentApplicationConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractInvoiceManage.ContractInvoiceManageDO;
import com.yaoan.module.econtract.dal.dataobject.contractPerformanceAcceptance.ContractPerformanceAcceptanceDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.SimpleContractMapper;
import com.yaoan.module.econtract.dal.mysql.contractPerformanceAcceptance.ContractPerformanceAcceptanceMapper;
import com.yaoan.module.econtract.dal.mysql.contractinvoicemanage.ContractInvoiceManageMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.StatusConstants;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import com.yaoan.module.econtract.service.codegeneration.CodeGenerationService;
import com.yaoan.module.econtract.service.contract.TaskService;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;


/**
 * 发票 Service 实现类
 *
 * @author lls
 */
@Slf4j
@Service
@Validated
public class ContractInvoiceManageServiceImpl implements ContractInvoiceManageService {

    @Resource
    private ContractInvoiceManageMapper contractInvoiceManageMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private CodeGenerationService codeGenerationService;
    @Resource
    private SimpleContractMapper simpleContractMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ContractPerformanceAcceptanceMapper contractPerformanceAcceptanceMapper;
    @Resource
    private TaskService taskService;
    static final String AUTO_REASON = "auto";

    @Override
    public String createContractInvoiceManage(ContractInvoiceManageSaveReqVO createReqVO) {
        //该计划不可重复创建收款申请
        Long count = contractPerformanceAcceptanceMapper.selectCount(new LambdaQueryWrapperX<ContractPerformanceAcceptanceDO>().eq(ContractPerformanceAcceptanceDO::getPlanId,createReqVO.getPlanId()));
        if(0 != count){
            throw exception(DIY_ERROR,"该计划已提交了收款申请，请勿重复提交");
        }
        //前面的计划如果没有完成，则不可发起该计划
        List<PaymentScheduleDO> oldPlans = paymentScheduleMapper.selectOldPlans(createReqVO.getContractId(),createReqVO.getSort());
        if(CollectionUtil.isNotEmpty(oldPlans)){
            List<Integer> todoPlanIds = oldPlans.stream().map(PaymentScheduleDO::getSort).sorted().collect(Collectors.toList());
            String sortStr = todoPlanIds.stream()
                    .map(String::valueOf)  // 将每个整数转换为字符串
                    .collect(Collectors.joining(",")); // 用逗号连接
            throw exception(DIY_ERROR,"第 "+ sortStr +" 期计划还未完成");
        }
        //校验验收申请
        List<ContractPerformanceAcceptanceDO> contractPerformanceAcceptanceDOS = contractPerformanceAcceptanceMapper.selectList(new LambdaQueryWrapperX<ContractPerformanceAcceptanceDO>()
                .eq(ContractPerformanceAcceptanceDO::getPlanId, createReqVO.getPlanId()).orderByAsc(ContractPerformanceAcceptanceDO::getStatus));
        //验收没有通过，不能进行付款申请
        if (ObjectUtil.isNotEmpty(contractPerformanceAcceptanceDOS)) {
            for (ContractPerformanceAcceptanceDO item : contractPerformanceAcceptanceDOS) {
                if (item.getStatus().equals(0)) {
                    throw exception(ErrorCodeConstants.PERFORM_TASK_NOT_APPROVAL);
                }
                if (item.getStatus().equals(2)) {
                    throw exception(ErrorCodeConstants.PERFORM_TASK_NOT_ACCEPTANCE_AGAIN);
                }
                if (item.getStatus().equals(1)) {
                    break;
                }
            }
        }
        // 插入
        ContractInvoiceManageDO contractInvoiceManage = BeanUtils.toBean(createReqVO, ContractInvoiceManageDO.class);
        CodeQueryReqVO codeQueryReqVO = new CodeQueryReqVO();
        codeQueryReqVO.setType("invoice");
        String code = codeGenerationService.generateCodeByVO(codeQueryReqVO);
        contractInvoiceManage.setCode(code);
        contractInvoiceManageMapper.insert(contractInvoiceManage);
        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();

        processInstanceVariables.put("entityType", 1);
        //获取流程key
        String key = ActivityConfigurationEnum.ECMS_CONTRACT_INVOICE.getDefinitionKey();
        if (ObjectUtil.isNotEmpty(contractInvoiceManage.getContractId())) {
            ContractType contractType = contractTypeMapper.selectById(contractInvoiceManage.getContractId());
            if (ObjectUtil.isNotEmpty(contractType) && ObjectUtil.isNotEmpty(contractType.getCollectionProcess())) {
                key = contractType.getCollectionProcess();
            }
        }
        // Map<String, Object> processInstanceVariables2 = BeanUtil.beanToMap(contractInvoiceManage);
        String processInstanceId = processInstanceApi.createProcessInstance(getLoginUserId(),
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(key)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(contractInvoiceManage.getId())));
        contractInvoiceManageMapper.updateById(new ContractInvoiceManageDO().setId(contractInvoiceManage.getId()).setProcessInstanceId(processInstanceId));
        //对应的收款计划的状态要改成“执行中”
        PaymentScheduleDO scheduleDO = new PaymentScheduleDO().setId(createReqVO.getPlanId()).setStatus(PaymentScheduleStatusEnums.DOING.getCode());
        paymentScheduleMapper.updateById(scheduleDO);
        //是否直接通过
        if(ObjectUtil.isNotEmpty(createReqVO.getIsSubmit()) && 1 == createReqVO.getIsSubmit()){
            //添加taskId
            if(ObjectUtil.isNotEmpty(processInstanceId)){
                List<String> processInstanceIds = Arrays.asList(processInstanceId);
                List<BpmTaskAllInfoRespVO> instanceIds = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(getLoginUserId(), processInstanceIds);
                if(ObjectUtil.isNotEmpty(instanceIds)){
                    for(BpmTaskAllInfoRespVO bpmTaskRespDTO:instanceIds){
                        BpmTaskApproveReqVO taskApproveReqVO = new BpmTaskApproveReqVO().setTaskId(bpmTaskRespDTO.getTaskId()).setReason(AUTO_REASON);
                        try {
                            taskService.approveTask(getLoginUserId(), taskApproveReqVO);
                        } catch (Exception e) {
                            log.error("approveTask异常:"+e.getMessage());
                            throw exception(DIY_ERROR,"请稍后重试或联系管理员处理。");
                        }
                    }

                }
            }
        }
        // 返回
        return contractInvoiceManage.getId();
    }

    @Override
    public void updateContractInvoiceManage(ContractInvoiceManageSaveReqVO updateReqVO) {
        // 校验存在
        validateContractInvoiceManageExists(updateReqVO.getId());
        // 更新
        ContractInvoiceManageDO updateObj = BeanUtils.toBean(updateReqVO, ContractInvoiceManageDO.class);
        contractInvoiceManageMapper.updateById(updateObj);
    }

    @Override
    public void deleteContractInvoiceManage(String id) {
        // 校验存在
        validateContractInvoiceManageExists(id);
        //对应的收款计划的状态要改成“未开始”
        ContractInvoiceManageDO contractInvoiceManageDO = contractInvoiceManageMapper.selectById(id);
        PaymentScheduleDO scheduleDO = new PaymentScheduleDO().setId(contractInvoiceManageDO.getPlanId()).setStatus(PaymentScheduleStatusEnums.TO_DO.getCode());
        paymentScheduleMapper.updateById(scheduleDO);
        // 删除
        contractInvoiceManageMapper.deleteById(id);
    }

    private void validateContractInvoiceManageExists(String id) {
        if (contractInvoiceManageMapper.selectById(id) == null) {
            throw exception(SYSTEM_ERROR, "申请不存在！");
        }
    }

    @Override
    public ContractInvoiceManageRespVO getContractInvoiceManage(String id) {
        ContractInvoiceManageDO contractInvoiceManageDO = contractInvoiceManageMapper.selectById(id);
        ContractInvoiceManageRespVO contractInvoiceManageRespVO = BeanUtils.toBean(contractInvoiceManageDO, ContractInvoiceManageRespVO.class);
        if (ObjectUtil.isNotNull(contractInvoiceManageRespVO.getContractId())) {
            LambdaQueryWrapperX<SimpleContractDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
            lambdaQueryWrapperX.eq(SimpleContractDO::getId, contractInvoiceManageRespVO.getContractId());
            List<SimpleContractDO> simpleContractDOList = simpleContractMapper.selectList(lambdaQueryWrapperX);
            enhanceContract(simpleContractDOList);
            contractInvoiceManageRespVO.setContractList(simpleContractDOList);
            if(ObjectUtil.isNotEmpty(contractInvoiceManageDO.getPlanId())){
                List<PaymentPlanRespVO> paymentPlanRespVOS = enhancePaymentPlan(contractInvoiceManageDO.getPlanId());
                contractInvoiceManageRespVO.setPaymentPlanRespVOList(paymentPlanRespVOS);
            }
        }
        //申请人
        AdminUserRespDTO userRespDTO = adminUserApi.getUser(Long.valueOf(contractInvoiceManageRespVO.getCreator()));
        if(ObjectUtil.isNotNull(userRespDTO)){
            contractInvoiceManageRespVO.setCreatorName(userRespDTO.getNickname());
        }
        return contractInvoiceManageRespVO;
    }

    private void enhanceContract(List<SimpleContractDO> simpleContractDOList) {
        if(CollectionUtil.isEmpty(simpleContractDOList)){
            return;
        }
        List<String> contractTypes = simpleContractDOList.stream().map(SimpleContractDO::getContractType).collect(Collectors.toList());
        List<ContractType> contractTypeList = contractTypeMapper.selectList(ContractType::getId,contractTypes);
        Map<String,ContractType> contractTypeMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(contractTypeList)){
            contractTypeMap = CollectionUtils.convertMap(contractTypeList,ContractType::getId);
        }
        for (SimpleContractDO contractDO : simpleContractDOList) {
            ContractType contractType = contractTypeMap.get(contractDO.getId());
            if(ObjectUtil.isNotNull(contractType)){
                contractDO.setContractType(contractType.getName());
            }
        }
    }

    private List<PaymentPlanRespVO> enhancePaymentPlan(String id) {
        List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectList(PaymentScheduleDO::getId, id);
        if (CollectionUtil.isEmpty(paymentScheduleDOList)) {
            return Collections.emptyList();
        }
        return PaymentApplicationConverter.INSTANCE.convertListSchedule2Resp(paymentScheduleDOList);
    }

    @Override
    public PageResult<ContractInvoiceManageRespVO> getContractInvoiceManagePage(ContractInvoiceManagePageReqVO pageReqVO) {
//        PageResult<ContractInvoiceManageDO> contractInvoiceManageDOPageResult = contractInvoiceManageMapper.selectPage(pageReqVO);
//        PageResult<ContractInvoiceManageRespVO> contractInvoiceManageRespVOPageResult = BeanUtils.toBean(contractInvoiceManageDOPageResult, ContractInvoiceManageRespVO.class);
//        return enhancePage(contractInvoiceManageRespVOPageResult);
        if (pageReqVO.getStatus() != null) {
            PageResult<ContractInvoiceManageDO> contractInvoiceManageDOPageResult = contractInvoiceManageMapper.selectPage(pageReqVO);
            PageResult<ContractInvoiceManageRespVO> contractInvoiceManageRespVOPageResult = BeanUtils.toBean(contractInvoiceManageDOPageResult, ContractInvoiceManageRespVO.class);
            return enhanceBpmAndContractPage(contractInvoiceManageRespVOPageResult, new HashMap<>());
        }
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        // 查询待办任务
        if (ObjectUtil.isNotNull(pageReqVO.getTaskResult())) {
            taskResult = pageReqVO.getTaskResult();
        }
        //查询合同类型表，取收款流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .eq(ContractType::getTypeStatus, 1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getCollectionProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        //获得已处理任务数据(已过滤掉已取消的任务)
        // List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.ECMS_CONTRACT_INVOICE.getDefinitionKey(), taskResult);
        //        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.PAYMENT_APPLICATION_APPROVE.getDefinitionKey());
        if(ObjectUtil.isNotEmpty(definitionKeys)){
            if (ApprovePageFlagEnums.TO_DO.getCode() == pageReqVO.getResult()) {
                processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(definitionKeys.get(0), definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
            } else if (ApprovePageFlagEnums.DONE.getCode() == pageReqVO.getResult()) {
                processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(definitionKeys.get(0), taskResult, definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
            } else {
                processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(definitionKeys.get(0), definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
            }
        }else {
            if (ApprovePageFlagEnums.TO_DO.getCode() == pageReqVO.getResult()) {
                processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.ECMS_CONTRACT_INVOICE.getDefinitionKey());
            } else if (ApprovePageFlagEnums.DONE.getCode() == pageReqVO.getResult()) {
                processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.ECMS_CONTRACT_INVOICE.getDefinitionKey(),taskResult);
            } else {
                processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.ECMS_CONTRACT_INVOICE.getDefinitionKey());
            }
        }

        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageReqVO.setInstanceIdList(instanceIdList);
        if(instanceIdList.isEmpty()){
            return PageResult.empty();
        }
        PageResult<ContractInvoiceManageDO> undoPageResult = contractInvoiceManageMapper.selectPage(pageReqVO);
        PageResult<ContractInvoiceManageRespVO> contractInvoiceManageRespVOPageResult = BeanUtils.toBean(undoPageResult, ContractInvoiceManageRespVO.class);
        return enhanceBpmAndContractPage(contractInvoiceManageRespVOPageResult, instanceRelationInfoRespDTOMap);

    }

    private PageResult<ContractInvoiceManageRespVO> enhanceBpmAndContractPage(PageResult<ContractInvoiceManageRespVO> contractInvoiceManagePageReqVOPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> contractProcessInstanceRelationInfoRespDTOMap) {
        if (CollectionUtil.isEmpty(contractInvoiceManagePageReqVOPageResult.getList())) {
            return contractInvoiceManagePageReqVOPageResult;
        }
        List<ContractInvoiceManageRespVO> contractInvoiceManageRespVOList = contractInvoiceManagePageReqVOPageResult.getList();
        List<String> userIds = contractInvoiceManageRespVOList.stream().map(ContractInvoiceManageRespVO::getCreator).collect(Collectors.toList());
        List<Long> longUserIds = userIds.stream()
                .map(Long::parseLong)  // Convert each String to Long
                .collect(Collectors.toList());
        Map<Long,AdminUserRespDTO> userRespDTOLMap = adminUserApi.getUserMap(longUserIds);
        List<String> contractIds = contractInvoiceManageRespVOList.stream().map(ContractInvoiceManageRespVO::getContractId).collect(Collectors.toList());
        LambdaQueryWrapperX<SimpleContractDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.in(SimpleContractDO::getId, contractIds);
        List<SimpleContractDO> simpleContractDOList = simpleContractMapper.selectList(lambdaQueryWrapperX);
        Map<String, SimpleContractDO> simpleContractDOMap = CollectionUtils.convertMap(simpleContractDOList, SimpleContractDO::getId);
        //获取支付期数
        List<String> planIds = contractInvoiceManageRespVOList.stream().map(ContractInvoiceManageRespVO::getPlanId).collect(Collectors.toList());
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(PaymentScheduleDO::getId, planIds);
        Map<String, PaymentScheduleDO> paymentDOMap = CollectionUtils.convertMap(paymentScheduleDOS, PaymentScheduleDO::getId);
        if (CollectionUtil.isNotEmpty(contractInvoiceManageRespVOList)) {
            contractInvoiceManageRespVOList.forEach(contractInvoiceManageRespVO -> {
                ContractProcessInstanceRelationInfoRespDTO dto = contractProcessInstanceRelationInfoRespDTOMap.get(contractInvoiceManageRespVO.getProcessInstanceId());
                AdminUserRespDTO user=userRespDTOLMap.get(Long.valueOf(contractInvoiceManageRespVO.getCreator()));
                if(ObjectUtil.isNotNull(user)) {
                    contractInvoiceManageRespVO.setCreatorName(user.getNickname());
                }
                if (ObjectUtil.isNotNull(dto)) {
                    contractInvoiceManageRespVO.setTaskId(dto.getTaskId());
                }
                if (simpleContractDOMap.get(contractInvoiceManageRespVO.getContractId()) != null) {
                    SimpleContractDO tempDO = simpleContractDOMap.get(contractInvoiceManageRespVO.getContractId());
                    contractInvoiceManageRespVO.setContractCode(tempDO.getCode());
                    contractInvoiceManageRespVO.setContractName(tempDO.getName());
                }
                if (paymentDOMap.get(contractInvoiceManageRespVO.getPlanId()) != null) {
                    contractInvoiceManageRespVO.setSort(paymentDOMap.get(contractInvoiceManageRespVO.getPlanId()).getSort());
                }
            });
        }


        return contractInvoiceManagePageReqVOPageResult;
    }


}