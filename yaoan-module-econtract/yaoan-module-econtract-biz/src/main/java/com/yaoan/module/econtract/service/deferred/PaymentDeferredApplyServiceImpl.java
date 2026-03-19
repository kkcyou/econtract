package com.yaoan.module.econtract.service.deferred;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.api.task.dto.v2.BpmTaskApproveReqDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.payment.deferred.vo.*;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.BigPaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.PayeeInfoRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.PaymentPlanRespVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativeByUserRespVO;
import com.yaoan.module.econtract.convert.businessfile.BusinessFileConverter;
import com.yaoan.module.econtract.convert.config.SystemConfigDTOConverter;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.convert.payment.PaymentApplicationConverter;
import com.yaoan.module.econtract.convert.payment.deferred.PaymentDeferredApplyConverter;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.deferred.PaymentDeferredApplyDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.deferred.PaymentDeferredApplyMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleApplyStatusEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import com.yaoan.module.econtract.enums.payment.PaymentTypeEnums;
import com.yaoan.module.econtract.enums.payment.SettlementMethodEnums;
import com.yaoan.module.econtract.service.businessfile.BusinessFileService;
import com.yaoan.module.econtract.service.relative.RelativeService;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.flowable.FlowableUtil;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import liquibase.pro.packaged.O;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/30 16:28
 */
@Service
public class PaymentDeferredApplyServiceImpl implements PaymentDeferredApplyService {
    @Resource
    private PaymentDeferredApplyMapper paymentDeferredApplyMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private BusinessFileService businessFileService;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private RelativeService relativeService;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private SystemConfigApi systemConfigApi;

    @Resource
    private BpmTaskApi taskApi;

    @Override
    public PageResult<PaymentDeferredListRespVO> listDeferredApplication(PaymentDeferredListReqVO vo) {
        PageResult<PaymentDeferredApplyDO> paymentApplicationDOList = paymentDeferredApplyMapper.selectPage(vo);
        PageResult<PaymentDeferredListRespVO> respVOPageResult = PaymentDeferredApplyConverter.INSTANCE.convertPageDO2Resp(paymentApplicationDOList);
        return enhancePage(respVOPageResult);
    }

    @Override
    @Transactional
    public String save2submit(PaymentDeferredApplySaveReqVO vo) {
        String id = saveOrUpdate(vo);
        // 如果存在任务id证明已有工作流，直接提交
        if (StringUtils.isNotEmpty(vo.getTaskId())){
            taskApi.approveTask(getLoginUserId(), new BpmTaskApproveReqDTO().setId(vo.getTaskId()));
            return id;
        }
        return submit(id);
    }

    /**
     * 分页的增强
     */
    private PageResult<PaymentDeferredListRespVO> enhancePage(PageResult<PaymentDeferredListRespVO> respVOPageResult) {
        Long loginId = getLoginUserId();
        List<PaymentDeferredListRespVO> list = respVOPageResult.getList();
        List<String> instanceList = list.stream().map(PaymentDeferredListRespVO::getProcessInstanceId).collect(Collectors.toList());
        List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

        Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        //待处理的任务
        List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
        Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        if (CollectionUtil.isNotEmpty(instanceList)) {
            taskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(loginId, instanceList);
            taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
            taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

            toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTime(taskAllInfoRespVOList);
            toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
        }
        for (PaymentDeferredListRespVO respVO : list) {
            //编号
            respVO.setPaymentApplyCode(respVO.getCode());
            //申请人
            AdminUserRespDTO user = adminUserApi.getUser(Long.valueOf(respVO.getApplyUserId()));
            if (ObjectUtil.isNotNull(user)) {
                respVO.setApplyUserName(user.getNickname());
            }
            CommonFlowableReqVOResultStatusEnums resultStatusEnums = CommonFlowableReqVOResultStatusEnums.getInstance(respVO.getResult());
            if (ObjectUtil.isNotNull(resultStatusEnums)) {
                respVO.setResultStr(resultStatusEnums.getInfo());
            }
            BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
            if (ObjectUtil.isNotNull(resultEnum)) {
                if (BpmProcessInstanceResultEnum.PROCESS == resultEnum) {
                    //审批中的任务分为 被退回 和审批中
                    BpmTaskAllInfoRespVO task = taskMap.get(respVO.getProcessInstanceId());
                    respVO.setTaskId(task.getTaskId());
                } else {
                    respVO.setResultStr(resultEnum.getDesc());
                }
                //如果是被退回的申请，回显任务id
                if (BpmProcessInstanceResultEnum.BACK == resultEnum) {
                    BpmTaskAllInfoRespVO rejectedTask = taskMap.get(respVO.getProcessInstanceId());
                    respVO.setTaskId(rejectedTask.getTaskId());
                }
                if (BpmProcessInstanceResultEnum.APPROVE != resultEnum) {
                    BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = toDoTaskMap.get(respVO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
                        respVO.setAssigneeId(bpmTaskAllInfoRespVO.getAssigneeUserId());
                    }
                }
            }
        }
        return respVOPageResult;
    }


    @Override
    public String saveOrUpdate(PaymentDeferredApplySaveReqVO vo) {
        PaymentDeferredApplyDO deferredApplyDO = PaymentDeferredApplyConverter.INSTANCE.saveReq2D(vo);
        deferredApplyDO.setApplyUserId(String.valueOf(getLoginUserId()));
        deferredApplyDO.setApplyUserName(adminUserApi.getUser(getLoginUserId()).getNickname());
        if (StringUtils.isBlank(vo.getId())) {
            paymentDeferredApplyMapper.insert(deferredApplyDO);
        } else {
            paymentDeferredApplyMapper.updateById(deferredApplyDO);
        }

        businessFileService.deleteByBusinessId(deferredApplyDO.getId());
        businessFileService.createBatchBusinessFile(deferredApplyDO.getId(), vo.getFileVOList());
        return deferredApplyDO.getId();
    }

    @Override
    public String update(PaymentDeferredApplyUpdateReqVO vo) {
        PaymentDeferredApplyDO deferredApplyDO = PaymentDeferredApplyConverter.INSTANCE.updateReq2D(vo);
        paymentDeferredApplyMapper.updateById(deferredApplyDO);
        return "success";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(String id) {
        //将原计划退回到未开始状态
        PaymentDeferredApplyDO paymentDeferredApplyDO = paymentDeferredApplyMapper.selectById(id);
        paymentScheduleMapper.updateById(new PaymentScheduleDO().setId(paymentDeferredApplyDO.getPlanId())
                .setStatus(PaymentScheduleStatusEnums.TO_DO.getCode())
                .setApplyStatus(PaymentScheduleApplyStatusEnums.NO_APPLY.getCode()));
        paymentDeferredApplyMapper.deleteById(id);
        return "success";
    }

    /**
     * @param id 计划id
     * @return
     */
    @Override
    public PaymentDeferredApplicationRespVO query(String id) {
         PaymentDeferredApplicationRespVO resultRespVO = new PaymentDeferredApplicationRespVO();
        
        PaymentDeferredApplyDO deferredApplyDO = paymentDeferredApplyMapper.selectOne(PaymentDeferredApplyDO::getId, id);
        PaymentDeferredInfoRespVO paymentDeferredInfoRespVO = PaymentDeferredApplyConverter.INSTANCE.convertDO2InfoResp(deferredApplyDO);
        PaymentScheduleDO paymentScheduleDO = paymentScheduleMapper.selectById(deferredApplyDO.getPlanId());
        if(ObjectUtil.isNotEmpty(paymentScheduleDO)){
            paymentDeferredInfoRespVO.setCurrentPayAmount(paymentScheduleDO.getAmount());
            paymentDeferredInfoRespVO.setOriginalPaymentDate(paymentScheduleDO.getPaymentTime());
        }
        resultRespVO.setPaymentDeferredInfoRespVO(paymentDeferredInfoRespVO);
        // 基本信息
        //PaymentApplicationBaseInfoV2RespVO baseInfoRespVO = enhanceBaseInfo(id, deferredApplyDO);

        ContractDO contractDO = contractMapper.selectOneByPlanId(deferredApplyDO.getPlanId());

        // 合同信息
        ContractInfoV2RespVO contractInfoRespVO = enhanceContractInfo(contractDO,deferredApplyDO.getPlanId());

        //附件
        List<BusinessFileVO> filesRespVOList = new ArrayList<>();
        List<BusinessFileDO> fileDOList = businessFileService.selectListByBusiness(id);
        filesRespVOList = BusinessFileConverter.INSTANCE.d2R(fileDOList);

        return resultRespVO.setContractInfoRespVO(contractInfoRespVO)
                .setFileRespVOList(filesRespVOList)
                ;
    }

    private PayeeInfoRespVO enhancePayeeInfo(ContractDO contractDO) {
        if (ObjectUtil.isNull(contractDO)) {
            return new PayeeInfoRespVO();
        }
        //如果是保存，就从相对方里取值
        List<Relative> relativeList = (List<Relative>) relativeMapper.selectListByContractId(contractDO.getId());
        if (CollectionUtil.isEmpty(relativeList)) {
            return new PayeeInfoRespVO();
        }
        Relative relative = relativeList.get(0);
        return new PayeeInfoRespVO().setName(relative.getCompanyName())
                .setBankName(relative.getBankName())
                .setBankAccount(relative.getBankAccount());
    }

    private ContractInfoV2RespVO enhanceContractInfo(ContractDO contractDO, String planId) {
        if (ObjectUtil.isNull(contractDO)) {
            return new ContractInfoV2RespVO();
        }
        ContractInfoV2RespVO respVO = ContractConverter.INSTANCE.convertDO2InfoV2(contractDO);
        ContractType contractType = contractTypeMapper.selectById(contractDO.getContractType());
        if (ObjectUtil.isNotNull(contractType)) {
            respVO.setContractType(contractType.getId());
            respVO.setContractTypeName(contractType.getName());
        }
        PaymentScheduleDO paymentScheduleDO = paymentScheduleMapper.selectOne(PaymentScheduleDO::getId, planId);
        AmountTypeEnums enums = AmountTypeEnums.getInstance(paymentScheduleDO.getAmountType());
        if (ObjectUtil.isNotNull(enums)) {
            String type = enums.getInfo();
            String str = contractDO.getName() + "合同 - 第" + paymentScheduleDO.getSort() + "期 - " + type;
            respVO.setPlans(str);
        }
        return respVO;
    }

    /**
     * 新需求没要求展示计划
     */
    private List<PaymentPlanRespVO> enhancePaymentPlan(String id) {
        List<PaymentScheduleDO> paymentScheduleDOList = new ArrayList<>();
        //草稿状态的申请的支付计划
        PaymentScheduleDO paymentScheduleDO = paymentScheduleMapper.selectById(id);
        if (ObjectUtil.isNull(paymentScheduleDO)) {
            return Collections.emptyList();
        }
        paymentScheduleDOList.add(paymentScheduleDO);
        return PaymentApplicationConverter.INSTANCE.convertListSchedule2Resp(paymentScheduleDOList);
    }

    private PaymentApplicationBaseInfoV2RespVO enhanceBaseInfo(String planId, PaymentDeferredApplyDO deferredApplyDO) {

        PaymentApplicationBaseInfoV2RespVO baseInfoRespVO = new PaymentApplicationBaseInfoV2RespVO().setApplyTime(LocalDateTime.now());
        PaymentScheduleDO scheduleDO = paymentScheduleMapper.selectById(planId);
        if (ObjectUtil.isNotNull(scheduleDO)) {
        }
        baseInfoRespVO.setCurrentPayAmount(scheduleDO.getAmount());
        baseInfoRespVO.setPaymentTime(scheduleDO.getPaymentTime());
        AdminUserRespDTO userRespDTO = adminUserApi.getUser(SecurityFrameworkUtils.getLoginUserId());
        if (ObjectUtil.isNotNull(userRespDTO)) {
            baseInfoRespVO.setApplicantId(String.valueOf(userRespDTO.getId()));
            baseInfoRespVO.setApplicantName(userRespDTO.getNickname());

            if (ObjectUtil.isNotNull(deferredApplyDO)) {
                baseInfoRespVO.setDeferredPaymentDate(deferredApplyDO.getDeferredPaymentDate());
                baseInfoRespVO.setReason(deferredApplyDO.getReason());
                baseInfoRespVO.setTitle(deferredApplyDO.getTitle());
            }

        }
        return baseInfoRespVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submit(String id) {
        checkScheduleForSubmit(id);

        Long loginUserId = getLoginUserId();

        //审批前前判断申请是否存在
        PaymentDeferredApplyDO paymentApplicationDO = paymentDeferredApplyMapper.selectById(id);

        //1.插入请求单
        //校验是否发起过审批
        paymentApplicationDO = paymentDeferredApplyMapper.selectById(id);
        CommonFlowableReqVOResultStatusEnums resultStatusEnums = CommonFlowableReqVOResultStatusEnums.getInstance(paymentApplicationDO.getResult());
        if (CommonFlowableReqVOResultStatusEnums.TO_SEND != resultStatusEnums) {
            throw exception(ErrorCodeConstants.PAYMENT_APPLICATION_SEND_EXIST_ERROR);
        }
        paymentApplicationDO.setApplyTime(new Date());

        //生成付款申请编码
        paymentDeferredApplyMapper.updateById(paymentApplicationDO.setCode(buildPaymentApplicationCode()));

        // 2 发起申请 BPM
        // 2.1 流程变量
        Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
        processInstanceVariables.put("id", paymentApplicationDO.getId());
        // 2.2 流程实例id
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(ActivityConfigurationEnum.PAYMENT_PLAN_DEFERRED_APPLICATION.getDefinitionKey()).setVariables(processInstanceVariables).setBusinessKey(id));
        //更新付款申请信息
        paymentDeferredApplyMapper.updateById(new PaymentDeferredApplyDO()
                .setResult(CommonFlowableReqVOResultStatusEnums.APPROVING.getResultCode())
                .setFlowStatus(StatusEnums.APPROVING.getCode())
                .setApplyTime(new Date())
                .setId(id)
                .setProcessInstanceId(processInstanceId)
        );

        //更新付款计划信息
        List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.selectPlanForApplication(id);
        if (CollectionUtil.isNotEmpty(scheduleDOList)) {
            scheduleDOList.forEach(paymentScheduleDO -> paymentScheduleDO
                    .setStatus(PaymentScheduleStatusEnums.DOING.getCode())
                    .setApplyStatus(PaymentScheduleApplyStatusEnums.APPLY.getCode())
            );
            paymentScheduleMapper.updateBatch(scheduleDOList);
        }
        return id;
    }

    /**
     * 自动生成 付款编码：提交时自动生成，
     * 生成规则：公司名称首字母缩写+年月日+时间戳
     */
    private String buildPaymentApplicationCode() {
        RelativeByUserRespVO relative = relativeService.queryRelativeByLoginUser();
        if (ObjectUtil.isNotNull(relative)) {
            return EcontractUtil.getCodeAutoByTimestamp(relative.getCompanyName());
        }
        return "queryRelativeByLoginUser-error";
    }

    /**
     * 校验该计划是否存在审批中的延期申请
     */
    private void checkScheduleForSubmit(String planId) {
        Long count = paymentDeferredApplyMapper.selectCount(new LambdaQueryWrapperX<PaymentDeferredApplyDO>()
                .eq(PaymentDeferredApplyDO::getPlanId, planId)
                .eq(PaymentDeferredApplyDO::getResult, BpmProcessInstanceResultEnum.PROCESS)
        );
        if (0 < count) {
            throw exception(SYSTEM_ERROR, "该计划已提起延期申请，不可重复提交。");
        }
    }

    /**
     * 审批-待办-列表（审批人查看 ）
     */
    @Override
    public BigPaymentApplicationListBpmRespVO getBpmToDoTaskPage(Long loginUserId, PaymentApplicationListBpmReqVO pageVO) {
        // 查询待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.PAYMENT_PLAN_DEFERRED_APPLICATION.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<PaymentDeferredApplyDO> doPageResult = paymentDeferredApplyMapper.selectApprovePage(pageVO, true);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }


    @Override
    public BigPaymentApplicationListBpmRespVO getBpmDoneTaskPage(Long loginUserId, PaymentApplicationListBpmReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        // 查询待办任务
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        //获得已处理任务数据(已过滤掉已取消的任务)
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.PAYMENT_PLAN_DEFERRED_APPLICATION.getDefinitionKey(), taskResult);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 排除待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> todoList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.PAYMENT_PLAN_DEFERRED_APPLICATION.getDefinitionKey());
        // 获得 ProcessInstance
        List<String> todoIds = todoList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        instanceIdList.removeAll(todoIds);
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<PaymentDeferredApplyDO> doPageResult = paymentDeferredApplyMapper.selectApprovePage(pageVO, false);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public BigPaymentApplicationListBpmRespVO getBpmAllTaskPage(Long loginUserId, PaymentApplicationListBpmReqVO pageVO) {
        // 查询所有任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.PAYMENT_PLAN_DEFERRED_APPLICATION.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<PaymentDeferredApplyDO> doPageResult = paymentDeferredApplyMapper.selectApprovePage(pageVO, true);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    private BigPaymentApplicationListBpmRespVO enhanceBpmPage(PageResult<PaymentDeferredApplyDO> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {

        List<PaymentDeferredApplyDO> doList = doPageResult.getList();
        List<PaymentApplicationListBpmRespVO> respVOList = PaymentDeferredApplyConverter.INSTANCE.convertBpmDO2Resp(doList);
        if (CollectionUtil.isNotEmpty(doList)) {


            //流程信息（业务和流程同表）
            List<String> doIdList = doList.stream().map(PaymentDeferredApplyDO::getId).collect(Collectors.toList());
            Map<String, PaymentDeferredApplyDO> bpmDOMap = CollectionUtils.convertMap(doList, PaymentDeferredApplyDO::getId);
            List<String> instanceList = doList.stream().map(PaymentDeferredApplyDO::getProcessInstanceId).filter(Objects::nonNull).collect(Collectors.toList());

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
            Map<String, BpmTaskAllInfoRespVO> finalTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();

            List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            if (CollectionUtil.isNotEmpty(instanceList)) {
                originalTaskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(originalTaskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTimeByUserId(originalTaskAllInfoRespVOList, getLoginUserId());
                toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                taskDoneEndTimeAllInfoRespVOList = EcontractUtil.distinctDoneTaskLatestEndTime(originalTaskAllInfoRespVOList);
                taskDoneEndTimeMap = CollectionUtils.convertMap(taskDoneEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
                finalTaskMap = CollectionUtils.convertMap(EcontractUtil.distinctTask(bpmTaskApi.getAllTaskIdByProcessInstanceIds(instanceList)).stream().filter(item->ObjectUtil.isNotEmpty(item.getProcessInstanceId())).collect(Collectors.toList()), BpmTaskAllInfoRespVO::getProcessInstanceId);
            }

            //有结束时间的流程任务
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskEndTimeAllInfoRespVOList = EcontractUtil.distinctTaskHaveEndTime(originalTaskAllInfoRespVOList);
                taskEndTimeMap = CollectionUtils.convertMap(taskEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }

            for (PaymentApplicationListBpmRespVO respVO : respVOList) {
                //申请人
                AdminUserRespDTO user = adminUserApi.getUser(Long.valueOf(respVO.getApplicantId()));
                if (ObjectUtil.isNotNull(user)) {
                    respVO.setApplicantName(user.getNickname());
                }
                //审批状态
                BpmTaskAllInfoRespVO toDoTaskAllInfoRespVO = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(toDoTaskAllInfoRespVO)) {
                    respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTaskAllInfoRespVO.getAssigneeUserId()));
                } else {
                    respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
                }

                // 状态返回
                BpmTaskAllInfoRespVO nowTaskAllInfoRespVO = finalTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(nowTaskAllInfoRespVO)) {
                    respVO.setStatusName(nowTaskAllInfoRespVO.getName());
                }

                SettlementMethodEnums settlementMethodEnums = SettlementMethodEnums.getInstance(respVO.getSettlementMethod());
                if (ObjectUtil.isNotNull(settlementMethodEnums)) {
                    respVO.setSettlementMethodName(settlementMethodEnums.getInfo());
                }
                PaymentTypeEnums paymentTypeEnums = PaymentTypeEnums.getInstance(respVO.getPaymentType());
                if (ObjectUtil.isNotNull(paymentTypeEnums)) {
                    respVO.setPaymentTypeName(paymentTypeEnums.getInfo());
                }
                BpmProcessInstanceResultEnum bpmProcessInstanceResultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
                if (ObjectUtil.isNotNull(bpmProcessInstanceResultEnum)) {
                    respVO.setResultStr(bpmProcessInstanceResultEnum.getDesc());
                }
                //流程任务
                ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO = instanceRelationInfoRespDTOMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)) {
                    respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
                }
                //历史任务信息（所有审批人）
                BpmTaskAllInfoRespVO historyTask = taskEndTimeMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(historyTask)) {
                    respVO.setHisTaskResult(historyTask.getResult());
                }
                //全部审批列表的审批状态(找不到当前登录人的待办任务，既是已办任务)
                BpmTaskAllInfoRespVO toDoTask = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(toDoTask)) {
                    respVO.setAssigneeId(toDoTask.getAssigneeUserId());
                    respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTask.getAssigneeUserId()));

                }
                //审批状态(全部里)
                else {
                    respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
                }
                //已审批任务的状态赋值（当前人对该审批的最近的一次操作结果）
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = instanceRelationInfoRespDTOMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setDoneTaskResult(infoRespDTO.getProcessResult());
                }
            }
            PageResult<PaymentApplicationListBpmRespVO> pageResult = new PageResult<PaymentApplicationListBpmRespVO>();
            pageResult.setList(respVOList).setTotal(doPageResult.getTotal());
            BigPaymentApplicationListBpmRespVO result = new BigPaymentApplicationListBpmRespVO().setPageResult(pageResult);
            //获取配置
            result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE)));
            return result;
        }
        BigPaymentApplicationListBpmRespVO result = new BigPaymentApplicationListBpmRespVO()
                .setPageResult(new PageResult<PaymentApplicationListBpmRespVO>()
                        .setList(Collections.emptyList())
                        .setTotal(doPageResult.getTotal())
                );
        result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE)));
        return result;
    }
}
