package com.yaoan.module.econtract.service.contract.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.bpm.api.bpm.activity.BpmActivityApi;
import com.yaoan.module.bpm.api.bpm.activity.dto.ActProcDefDTO;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.*;
import com.yaoan.module.bpm.api.task.dto.v2.BpmTaskApproveReqDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.enums.task.ContractProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.dal.dataobject.bpm.archive.BpmContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.model.ModelBpmDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.register.BpmContractRegisterDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.template.TemplateBpmDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.dal.dataobject.copyrecipients.BpmCopyRecipientsDO;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.relativeContact.RelativeContact;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.mysql.bpm.archive.BpmContractArchivesMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contractborrow.ContractBorrowBpmMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.model.ModelBpmMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.register.BpmContractRegisterMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.template.TemplateBpmMapper;
import com.yaoan.module.econtract.dal.mysql.change.BpmContractChangeMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contractarchives.ContractArchivesMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import com.yaoan.module.econtract.dal.mysql.copyrecipients.BpmCopyRecipientsMapper;
import com.yaoan.module.econtract.dal.mysql.model.SimpleModelMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplicationMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.relativeContact.RelativeContactMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.FileVersionEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleApplyStatusEnums;
import com.yaoan.module.econtract.framework.core.event.version.FileVersionEvent;
import com.yaoan.module.econtract.framework.core.event.version.FileVersionEventPublisher;
import com.yaoan.module.econtract.framework.core.event.warning.EcmsWarningEvent;
import com.yaoan.module.econtract.framework.core.event.warning.EcmsWarningEventPublisher;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.econtract.service.contract.TaskService;
import com.yaoan.module.econtract.service.cx.ChangXieService;
import com.yaoan.module.econtract.service.relative.RelativeService;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yaoan.module.bpm.enums.model.FlowableModelEnums.BOTH_CONFIRM_02;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_DATA_ERROR;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_REQUIRE_PARAM_ERROR;

/**
 * @author doujiale
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Resource
    private BpmTaskApi taskApi;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private RelativeContactMapper relativeContactMapper;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private RelativeService relativeService;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;
    @Resource
    private BpmContractRegisterMapper bpmContractRegisterMapper;
    @Resource
    private SimpleModelMapper simpleModelMapper;
    @Resource
    private ModelBpmMapper modelBpmMapper;
    @Resource
    private PaymentApplicationMapper paymentApplicationMapper;
    @Resource
    private TermMapper termMapper;
    @Resource
    private ContractTemplateMapper contractTemplateMapper;
    @Resource
    private TemplateBpmMapper templateBpmMapper;
    @Resource
    private BpmContractChangeMapper bpmContractChangeMapper;
    @Resource
    private BpmContractMapper bpmContractMapper;
    @Resource
    private ContractBorrowBpmMapper contractBorrowBpmMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private BpmCopyRecipientsMapper bpmCopyRecipientsMapper;
    @Resource
    private BpmActivityApi bpmActivityApi;
    @Resource
    private FileVersionEventPublisher fileVersionEventPublisher;
    @Resource
    private ChangXieService changXieService;
    @Resource
    private ContractService contractService;
    @Resource
    private ContractArchivesMapper contractArchivesMapper;
    @Resource
    private BpmContractArchivesMapper bpmContractArchivesMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private EcmsWarningEventPublisher warningEventPublisher;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveTask(Long userId, @Valid BpmTaskApproveReqVO reqVO) {

        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("passVal", ContractProcessInstanceResultEnum.CONFIRM.getCommand());
        //保存抄送内容
        saveCopyRecipients(reqVO.getTaskId(), BpmProcessInstanceResultEnum.APPROVE.getResult(), reqVO.getReason());
//        taskApi.handleTask(new BpmHandleTaskReqDTO().setUserId(userId).setTaskId(reqVO.getTaskId())
//                .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult()).setVariables(processInstanceVariables).setReason(reqVO.getReason()));
        // 消息提醒监控项处理
        BpmTaskRespDTO taskRespDTO = taskApi.getRunTaskRespDTO(reqVO.getTaskId());
        TaskForWarningReqDTO taskParams = new TaskForWarningReqDTO()
                .setProcessInstanceId(taskRespDTO.getProcessInstanceId());
        warningEventPublisher.setEvent(new EcmsWarningEvent(this).setFlag("1").setTaskParams(taskParams));

        BpmTaskApproveReqDTO reqDTO = new BpmTaskApproveReqDTO()
                .setId(reqVO.getTaskId())
                .setVariables(processInstanceVariables)
                .setReason(reqVO.getReason());
        taskApi.approveTask(userId, reqDTO);

        try {
            ActivityConfigurationEnum configurationEnum = ActivityConfigurationEnum.getInstanceByDefKey(reqVO.getProcessDefinition());
            // 文件留痕
            switch (configurationEnum) {
                case CONTRACT_DRAFT_APPROVE:
                    fileVersionEventPublisher.sendEvent(
                            new FileVersionEvent(this)
                                    .setBusinessId(reqVO.getBusinessId())
                                    .setBusinessType(FileVersionEnums.CONTRACT.getCode())
                                    .setRemark("审批通过")
                    );
                    break;
            }

            //如果是发起签署，则转成pdf
            List<String> taskIds = new ArrayList<String>();
            taskIds.add(reqVO.getTaskId());
            List<SimpleTaskDTO> taskDTOList = taskApi.getBpmTaskByTaskIds(taskIds);
            if (CollectionUtil.isNotEmpty(taskDTOList)) {
                SimpleTaskDTO taskDTO = taskDTOList.get(0);
                if (BOTH_CONFIRM_02.getName().equals(taskDTO.getName())) {
                    ContractDO contractDO = contractMapper.selectById(reqVO.getBusinessId());
                    if (ObjectUtil.isNotNull(contractDO)) {
//                        CXFileConverterReqVO converterReqVO=new CXFileConverterReqVO()
//                                .setKey(String.valueOf(contractDO.getFileAddId()))
//                                .setFiletype(FileTypeEnums.DOCX.getCode())
//                                ;
//
//                        changXieService.converter(converterReqVO);
                        Long pdfFileId = contractService.toPdf(new ContractToPdfVO().setName(contractDO.getName()).setFileAddId(contractDO.getFileAddId()));
                        FileDTO fileDTO = fileApi.selectById(pdfFileId);
                        if (ObjectUtil.isNotNull(fileDTO)) {
                            contractDO.setPdfFileId(pdfFileId)
                                    .setFileName(fileDTO.getName());
                            contractMapper.updateById(contractDO);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private void saveCopyRecipients(String taskId, Integer result, String reason) {
        //获取抄送规则
        List<String> taskIds = new ArrayList<String>();
        taskIds.add(taskId);
        List<SimpleTaskDTO> tasks = taskApi.getBpmTaskByTaskIds(taskIds);
        if (CollectionUtil.isNotEmpty(tasks)) {
            SimpleTaskDTO task = tasks.get(0);
            String definitionKey = "";
            if (ObjectUtil.isNotNull(task)) {

                ActProcDefDTO defDTO = bpmActivityApi.getActReProcdefByDefinitionId(task.getProcessDefinitionId());
                if (ObjectUtil.isNotNull(defDTO)) {
                    definitionKey = defDTO.getKey();
                }
                task.setProcessDefinitionKey(definitionKey);
                if (CollectionUtil.isNotEmpty(task.getCopyRecipientIds())) {
                    //保存抄送内容
                    List<BpmCopyRecipientsDO> copyRecipients = new ArrayList<BpmCopyRecipientsDO>();
                    for (Long recipientId : task.getCopyRecipientIds()) {
                        BpmCopyRecipientsDO copyRecipientsDO = new BpmCopyRecipientsDO();
                        String businessId = getBusinessId(task);
                        copyRecipientsDO.setBusinessId(businessId);
                        copyRecipientsDO.setResult(result);
                        copyRecipientsDO.setProcessDefinitionKey(definitionKey);
                        copyRecipientsDO.setRecipientId(recipientId);
                        copyRecipientsDO.setTaskId(task.getTaskId());
                        copyRecipients.add(copyRecipientsDO);
                    }
                    bpmCopyRecipientsMapper.insertBatch(copyRecipients);
                }
            }
        }
    }

    private String getBusinessId(SimpleTaskDTO task) {
        String businessId = "";
        String definitionKey = task.getProcessDefinitionKey();
        ActivityConfigurationEnum configurationEnum = ActivityConfigurationEnum.getInstanceByDefKey(definitionKey);
        if (ObjectUtil.isNull(configurationEnum)) {
            return null;
        }
        switch (configurationEnum) {
            // 合同草拟审批
            case CONTRACT_DRAFT_APPROVE:
                BpmContract contract = bpmContractMapper.selectOne(new LambdaQueryWrapperX<BpmContract>().eq(BpmContract::getProcessInstanceId, task.getProcessInstanceId()));
                if (ObjectUtil.isNotNull(contract)) {
                    businessId = contract.getContractId();
                }
                return businessId;
            // 模版审批
            case MODEL_APPROVE:
                ModelBpmDO modelBpmDO = modelBpmMapper.selectOne(new LambdaQueryWrapperX<ModelBpmDO>().eq(ModelBpmDO::getProcessInstanceId, task.getProcessInstanceId()));
                if (ObjectUtil.isNotNull(modelBpmDO)) {
                    businessId = modelBpmDO.getModelId();
                }
                return businessId;
            // 范本审批
            case TEMPLATE_APPROVE:
                TemplateBpmDO templateBpmDO = templateBpmMapper.selectOne(new LambdaQueryWrapperX<TemplateBpmDO>().eq(TemplateBpmDO::getProcessInstanceId, task.getProcessInstanceId()));
                if (ObjectUtil.isNotNull(templateBpmDO)) {
                    businessId = templateBpmDO.getTemplateId();
                }
                return businessId;
            // 合同登记审批
            case CONTRACT_REGISTER_APPLICATION_APPROVE:
                BpmContractRegisterDO registerDO = bpmContractRegisterMapper.selectOne(new LambdaQueryWrapperX<BpmContractRegisterDO>().eq(BpmContractRegisterDO::getProcessInstanceId, task.getProcessInstanceId()));
                if (ObjectUtil.isNotNull(registerDO)) {
                    businessId = registerDO.getContractId();
                }
                return businessId;

            // 合同借阅审批
            case CONTRACT_BORROW_SUBMIT_APPROVE:
                ContractBorrowBpmDO borrowBpmDO = contractBorrowBpmMapper.selectOne(new LambdaQueryWrapperX<ContractBorrowBpmDO>().eq(ContractBorrowBpmDO::getProcessInstanceId, task.getProcessInstanceId()));
                if (ObjectUtil.isNotNull(borrowBpmDO)) {
                    businessId = borrowBpmDO.getBorrowId();
                }
                return businessId;

            // 条款审批
            case TERM_APPLICATION_APPROVE:
                Term term = termMapper.selectOne(new LambdaQueryWrapperX<Term>().eq(Term::getProcessInstanceId, task.getProcessInstanceId()));
                if (ObjectUtil.isNotNull(term)) {
                    businessId = term.getId();
                }
                return businessId;
            // 付款申请审批
            case PAYMENT_APPLICATION_APPROVE:
                PaymentApplicationDO paymentApplicationDO = paymentApplicationMapper.selectOne(new LambdaQueryWrapperX<PaymentApplicationDO>().eq(PaymentApplicationDO::getProcessInstanceId, task.getProcessInstanceId()));
                if (ObjectUtil.isNotNull(paymentApplicationDO)) {
                    businessId = paymentApplicationDO.getId();
                }
                return businessId;
            // 合同变动审批
            case CONTRACT_CHANGE_APPLICATION_APPROVE:
                BpmContractChangeDO changeDO = bpmContractChangeMapper.selectOne(new LambdaQueryWrapperX<BpmContractChangeDO>().eq(BpmContractChangeDO::getProcessInstanceId, task.getProcessInstanceId()));
                if (ObjectUtil.isNotNull(changeDO)) {
                    businessId = changeDO.getContractId();
                }
                return businessId;
            default:
                return null;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectTask(Long userId, @Valid BpmTaskRejectReqVO reqVO) {

        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("passVal", ContractProcessInstanceResultEnum.REJECT.getCommand());

        //保存抄送内容
        saveCopyRecipients(reqVO.getTaskId(), BpmProcessInstanceResultEnum.BACK.getResult(), reqVO.getReason());
        // 2.2 更新 task 状态 + 原因
        taskApi.handleTask2(new BpmHandleTaskReqDTO().setUserId(userId).setTaskId(reqVO.getTaskId())
                .setResult(BpmProcessInstanceResultEnum.BACK.getResult()).setVariables(processInstanceVariables).setReason(reqVO.getReason()));

        try {
            // 文件留痕
            fileVersionEventPublisher.sendEvent(
                    new FileVersionEvent(this)
                            .setBusinessId(reqVO.getBusinessId())
                            .setBusinessType(FileVersionEnums.CONTRACT.getCode())
                            .setRemark("审批退回")
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendTask(Long userId, BpmTaskApproveReqVO reqVO) {

        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("passVal", ContractProcessInstanceResultEnum.SEND.getCommand());

        taskApi.handleTask2(new BpmHandleTaskReqDTO().setUserId(userId).setTaskId(reqVO.getTaskId())
                .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult()).setVariables(processInstanceVariables));

    }

    @Override
    public void revokeTask(Long userId, BpmTaskRevokeReqVO reqVO) {
        taskApi.revokeTask(userId, new TaskRevokeReqDTO().setTaskId(reqVO.getTaskId()).setProcessInstanceId(reqVO.getProcessInstanceId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void counterSignRevokeTask(Long userId, BpmTaskRevokeReqVO reqVO) {
        // 根据业务记录完成已完成的任务
        ActivityConfigurationEnum configurationEnum = ActivityConfigurationEnum.getInstanceByDefKey(reqVO.getProcessDefinition());
        if (ObjectUtil.isNotEmpty(configurationEnum)) {
            // 执行撤回
            taskApi.revokeTask(userId, new TaskRevokeReqDTO().setTaskId(reqVO.getTaskId()).setProcessInstanceId(reqVO.getProcessInstanceId()));

            switch (configurationEnum) {
                case ECMS_CONTRACT_RELATIVES:
                    DataPermissionUtils.executeIgnore(() -> {
                        TenantUtils.executeIgnore(() -> {
                            ContractDO contractDO = contractMapper.selectById(reqVO.getBusinessId());
                            if (ObjectUtil.isNull(contractDO)) {
                                throw exception(EMPTY_DATA_ERROR);
                            }
                            List<SignatoryRelDO> signatoryRelDOS = new ArrayList<>();
                            // 如果是撤回确认退回
                            if (ContractStatusEnums.BE_SENT_BACK.getCode().equals(contractDO.getStatus())) {
                                contractMapper.updateById(new ContractDO().setId(reqVO.getBusinessId()).setStatus(ContractStatusEnums.SENT.getCode()));
// 已处理过的相对方
                                signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, reqVO.getBusinessId(), SignatoryRelDO::getIsConfirm, IfNumEnums.YES.getCode());
                                // 排除当前操作人所属的相对方
                                List<Relative> relatives = relativeMapper.selectList4Relative(getLoginUserId());
                                String signatoryId = "";
                                if (CollectionUtil.isNotEmpty(relatives)) {
                                    signatoryId = relatives.get(0).getId();
                                    signatoryRelDOS.removeIf(signatoryRelDO -> signatoryRelDO.getSignatoryId().equals(relatives.get(0).getId()));
                                }
                                // 更新用户所属相对方表操作状态为未处理
                                List<SignatoryRelDO> signatoryRelDOList = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, signatoryId, SignatoryRelDO::getContractId, reqVO.getBusinessId());
                                if (CollectionUtil.isNotEmpty(signatoryRelDOList)) {
                                    signatoryRelMapper.updateById(new SignatoryRelDO().setId(signatoryRelDOList.get(0).getId()).setIsConfirm(IfNumEnums.NO.getCode()));
                                }


                            }
                            // 如果是撤回签署退回
                            if (ContractStatusEnums.SIGN_REJECTED.getCode().equals(contractDO.getStatus())) {
                                contractMapper.updateById(new ContractDO().setId(reqVO.getBusinessId()).setIsSign(IfNumEnums.YES.getCode()).setStatus(ContractStatusEnums.TO_BE_SIGNED.getCode()));
                                signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, reqVO.getBusinessId(), SignatoryRelDO::getIsSign, IfNumEnums.YES.getCode());
// 排除当前操作人所属的相对方
                                List<Relative> relatives = relativeMapper.selectList4Relative(getLoginUserId());
                                String signatoryId = "";
                                if (CollectionUtil.isNotEmpty(relatives)) {
                                    signatoryId = relatives.get(0).getId();
                                    signatoryRelDOS.removeIf(signatoryRelDO -> signatoryRelDO.getSignatoryId().equals(relatives.get(0).getId()));
                                }
                                // 更新用户所属相对方表操作状态为未处理
                                List<SignatoryRelDO> signatoryRelDOList = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, signatoryId, SignatoryRelDO::getContractId, reqVO.getBusinessId());
                                if (CollectionUtil.isNotEmpty(signatoryRelDOList)) {
                                    signatoryRelMapper.updateById(new SignatoryRelDO().setId(signatoryRelDOList.get(0).getId()).setIsSign(IfNumEnums.NO.getCode()));
                                }
                            }
                            // 查找当前流程实例中所有未完成的会签任务
                            if (ObjectUtil.isNotNull(signatoryRelDOS)) {
                                List<BpmTaskAllInfoRespVO> todoTaskList = taskApi.getTodoTaskList(reqVO.getProcessInstanceId());
                                Map<Long, String> tasks = todoTaskList.stream().collect(Collectors.toMap(BpmTaskAllInfoRespVO::getAssigneeUserId, BpmTaskAllInfoRespVO::getTaskId));
                                Map map = new HashMap();
                                map.put("committed", true);
                                for (SignatoryRelDO signatoryRelDO : signatoryRelDOS) {
                                    // 已经处理过的相对方，任务状态为完成
                                    List<Long> allContactId = relativeService.getAllContactId(signatoryRelDO.getSignatoryId());
                                    if (CollectionUtil.isNotEmpty(allContactId)) {
                                        tasks.forEach((k, v) -> {
                                            if (allContactId.contains(k)) {
                                                taskApi.setTaskVariable(map, v);
                                                taskApi.approveTask(k, new BpmTaskApproveReqDTO().setId(v));
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    });
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void repealTask(Long userId, BpmTaskRepealReqVO reqVO) {
//        bpmProcessInstanceApi.cancelContractProcessInstance(userId, new BpmProcessInstanceCancelReqDTO().setId(reqVO.getProcessInstanceId()).setReason("自主撤销"));
        ContractDO contractDO = contractMapper.selectOne(ContractDO::getProcessInstanceId, reqVO.getProcessInstanceId());
        if (contractDO != null) {
            contractDO.setStatus(ContractStatusEnums.TO_BE_SENT.getCode());
            contractMapper.updateById(contractDO);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repealTaskV2(Long userId, BpmTaskRepealReqVO reqVO) {
        //校验改流程是否可撤回
        if (!enableRepeal(reqVO)) {
//            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_OTHER_DONE);
        }
        //校验流程实例（撤回人必须是发起人，必须是运行中的流程） 并取消流程实例（只处理的工作流的基础表）
//        bpmProcessInstanceApi.cancelContractProcessInstance(userId, new BpmProcessInstanceCancelReqDTO().setId(reqVO.getProcessInstanceId()).setReason("自主撤销"));
        ActivityConfigurationEnum activityConfigurationEnum = ActivityConfigurationEnum.getInstanceByDefKey(reqVO.getProcessDefinition());
        if (ObjectUtil.isNotNull(activityConfigurationEnum)) {

            switch (activityConfigurationEnum) {
                //撤回合同起草申请
                case CONTRACT_DRAFT_APPROVE:
                    ContractDO contractDO = contractMapper.selectOne(ContractDO::getId, reqVO.getBusinessId());
                    if (contractDO != null) {
                        contractDO.setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());
                        contractMapper.updateById(contractDO);
                        //删除流程
                        BpmContract bpmContract = bpmContractMapper.selectOne(new LambdaQueryWrapperX<BpmContract>().eqIfPresent(BpmContract::getContractId, contractDO.getId()));
                        if (ObjectUtil.isNotNull(bpmContract)) {
                            bpmContractMapper.deleteById(bpmContract);
                        }

                    } else {
                        throw exception(EMPTY_DATA_ERROR);
                    }
                    return;

                //撤回范本申请
                case TEMPLATE_APPROVE:
                    ContractTemplate template = contractTemplateMapper.selectOne(ContractTemplate::getId, reqVO.getBusinessId());
                    if (ObjectUtil.isNotNull(template)) {
                        template.setApproveStatus(BpmProcessInstanceResultEnum.DRAFT.getResult());
                        contractTemplateMapper.updateById(template);
                    }
                    TemplateBpmDO templateBpmDO = templateBpmMapper.selectOne(TemplateBpmDO::getProcessInstanceId, reqVO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(templateBpmDO)) {
                        templateBpmMapper.delete(new LambdaQueryWrapperX<TemplateBpmDO>().eq(TemplateBpmDO::getProcessInstanceId, reqVO.getProcessInstanceId()));
                    } else {
                        throw exception(EMPTY_DATA_ERROR);
                    }
                    return;

                //撤回模板申请
                case MODEL_APPROVE:
                    SimpleModel model = simpleModelMapper.selectOne(SimpleModel::getId, reqVO.getBusinessId());
                    if (ObjectUtil.isNotNull(model)) {
                        model.setApproveStatus(BpmProcessInstanceResultEnum.DRAFT.getResult());
                        model.setStatus(BpmProcessInstanceResultEnum.DRAFT.getResult());
                        simpleModelMapper.updateById(model);
                    } else {
                        throw exception(EMPTY_DATA_ERROR);
                    }
                    ModelBpmDO modelBpmDO = modelBpmMapper.selectOne(ModelBpmDO::getProcessInstanceId, reqVO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(modelBpmDO)) {
                        modelBpmMapper.delete(new LambdaQueryWrapperX<ModelBpmDO>().eq(ModelBpmDO::getProcessInstanceId, reqVO.getProcessInstanceId()));
                    } else {
                        throw exception(EMPTY_DATA_ERROR);
                    }
                    return;

                //撤回履约终止申请
                case PERFORMANCE_SUSPEND_APPROVE:
                    return;

                //撤回付款申请
                case PAYMENT_APPLICATION_APPROVE: {
                    PaymentApplicationDO entity = paymentApplicationMapper.selectOne(PaymentApplicationDO::getId, reqVO.getBusinessId());
                    if (ObjectUtil.isNotNull(entity)) {
                        entity.setResult(BpmProcessInstanceResultEnum.DRAFT.getResult());
                        entity.setProcessInstanceId("");
                        //更新付款计划信息
                        List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.selectPlanForApplication(reqVO.getBusinessId());
                        if (CollectionUtil.isNotEmpty(scheduleDOList)) {
                            scheduleDOList.stream().forEach(paymentScheduleDO -> paymentScheduleDO
                                    .setApplyStatus(PaymentScheduleApplyStatusEnums.NO_APPLY.getCode())
                            );
                            paymentScheduleMapper.updateBatch(scheduleDOList);

                        } else {
                            throw exception(EMPTY_DATA_ERROR);
                        }
                        paymentApplicationMapper.updateById(entity);
                    }
                }
                return;

                //撤回条款
                case TERM_APPLICATION_APPROVE: {
                    Term entity = termMapper.selectOne(Term::getId, reqVO.getBusinessId());
                    if (ObjectUtil.isNotNull(entity)) {
                        entity.setResult(BpmProcessInstanceResultEnum.DRAFT.getResult());
                        //恢复未发布状态
                        entity.setStatus("0");
                        entity.setProcessInstanceId("");
                    } else {
                        throw exception(EMPTY_DATA_ERROR);
                    }
                    termMapper.updateById(entity);
                }
                return;

                //撤回合同借阅(jie的逻辑:借阅的提交和撤回不会影响合同表的字段)
                case CONTRACT_BORROW_SUBMIT_APPROVE: {
                    ContractBorrowBpmDO entity = contractBorrowBpmMapper.selectOne(ContractBorrowBpmDO::getId, reqVO.getBusinessId());
                    if (ObjectUtil.isNotNull(entity)) {
                        entity.setResult(BpmProcessInstanceResultEnum.BACK.getResult());
                        entity.setProcessInstanceId("");
                        contractBorrowBpmMapper.updateById(entity);
                    } else {
                        throw exception(EMPTY_DATA_ERROR);
                    }

                }
                return;

                //撤回合同变动
                case CONTRACT_CHANGE_APPLICATION_APPROVE: {
                    BpmContractChangeDO entity = bpmContractChangeMapper.selectOne(BpmContractChangeDO::getId, reqVO.getBusinessId());
                    if (ObjectUtil.isNotNull(entity)) {
                        entity.setResult(BpmProcessInstanceResultEnum.DRAFT.getResult());
                        entity.setProcessInstanceId("");
                    } else {
                        throw exception(EMPTY_DATA_ERROR);
                    }
                    bpmContractChangeMapper.updateById(entity);
                }
                return;

                //撤回合同归档
                case CONTRACT_ARCHIVE_APPLY: {
                    BpmContractArchivesDO entity = bpmContractArchivesMapper.selectOne(BpmContractArchivesDO::getId, reqVO.getBusinessId());
                    ContractArchivesDO archivesDO = new ContractArchivesDO();
                    if (entity.getType() == 1) {
                        archivesDO.setRejectType(1);
                        // 补充退回的
                    } else {
                        archivesDO.setRejectType(0);
                        // 归档退回的
                    }
                    archivesDO.setId(entity.getArchiveId());
                    archivesDO.setStatus(2); //草稿
                    contractArchivesMapper.updateById(archivesDO);

                    entity.setResult(BpmProcessInstanceResultEnum.REJECT.getResult());
                    entity.setProcessInstanceId("");
                    entity.setDeleted(true);
                    bpmContractArchivesMapper.updateById(entity);
                }
                return;

                //撤回合同登记
                case CONTRACT_REGISTER_APPLICATION_APPROVE: {
                    ContractDO contractDO1 = new ContractDO();
                    contractDO1.setId(reqVO.getBusinessId());
                    contractDO1.setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());
                    contractMapper.updateById(contractDO1);
                    BpmContractRegisterDO entity = new BpmContractRegisterDO().setProcessInstanceId(reqVO.getProcessInstanceId());
                    entity.setResult(BpmProcessInstanceResultEnum.DRAFT.getResult());
                    entity.setProcessInstanceId("");

                    bpmContractRegisterMapper.delete(new LambdaQueryWrapperX<BpmContractRegisterDO>().eq(BpmContractRegisterDO::getProcessInstanceId, reqVO.getProcessInstanceId()));

                }
                return;

                default:
                    return;
            }
        }

    }

    /**
     * 1，校验是否是再次提交
     * 2，校验流程是否被审批过
     */
    private Boolean enableRepeal(BpmTaskRepealReqVO reqVO) {
        String definitionKey = reqVO.getProcessDefinition();
        String processInstanceId = reqVO.getProcessInstanceId();

        //1，校验是否是发起人再次提交,则可以撤回
        if (taskApi.ifUpdaterSubmit(definitionKey, processInstanceId)) {
            return true;
        }

        //2，如果不是再次提交，还存在审批历史，则不可撤回
        if (taskApi.existApprovedHisTask(definitionKey, processInstanceId)) {
            return false;
        }

        return true;

    }

    /**
     * 批量通过请求
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String approveTaskBatch(List<BpmTaskApproveReqVO> voList) {
        if (CollectionUtil.isEmpty(voList)) {
            throw exception(EMPTY_REQUIRE_PARAM_ERROR);
        }
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        for (BpmTaskApproveReqVO item : voList) {
            approveTask(userId, item);
        }
        return "success";
    }

    /**
     * 批量退回请求
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String rejectTaskBatch(List<BpmTaskRejectReqVO> voList) {
        if (CollectionUtil.isEmpty(voList)) {
            throw exception(EMPTY_REQUIRE_PARAM_ERROR);
        }
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        for (BpmTaskRejectReqVO item : voList) {
            rejectTask(userId, item);
        }
        return "success";

    }
}
