package com.yaoan.module.econtract.service.change;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.*;
import com.yaoan.module.bpm.enums.model.FlowableModelEnums;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceStatusEnum;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.change.vo.*;
import com.yaoan.module.econtract.controller.admin.change.vo.changerisk.ApplicationRespVO;
import com.yaoan.module.econtract.controller.admin.change.vo.changerisk.ChangeRiskListRespVO;
import com.yaoan.module.econtract.controller.admin.change.vo.changerisk.ChangeRiskPlanRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.BpmTaskApproveReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.PaymentScheduleRespVO;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.AttachmentVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.convert.change.ChangeConverter;
import com.yaoan.module.econtract.convert.config.SystemConfigDTOConverter;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.convert.contract.PaymentScheduleConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangePaymentDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.ChangeElementDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.ElementDO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.mysql.agreement.PrefAgreementRelMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.dal.mysql.businessfile.BusinessFileMapper;
import com.yaoan.module.econtract.dal.mysql.category.ContractCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.change.BpmContractChangeMapper;
import com.yaoan.module.econtract.dal.mysql.change.BpmContractChangePaymentMapper;
import com.yaoan.module.econtract.dal.mysql.change.ChangeElementMapper;
import com.yaoan.module.econtract.dal.mysql.change.ElementMapper;
import com.yaoan.module.econtract.dal.mysql.contract.*;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.model.MyCollectModelMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplicationMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.terminate.TerminateContractMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.common.IfEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums;
import com.yaoan.module.econtract.enums.payment.CollectionTypeEnums;
import com.yaoan.module.econtract.enums.payment.MoneyTypeEnums;
import com.yaoan.module.econtract.enums.payment.PaymentApplicationStatusEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import com.yaoan.module.econtract.service.bpm.contract.BpmContractService;
import com.yaoan.module.econtract.service.contract.TaskService;
import com.yaoan.module.econtract.service.flow.FlowService;
import com.yaoan.module.econtract.service.performance.contractPerformance.ContractPerfService;
import com.yaoan.module.econtract.service.performance.perfTask.PerfTaskService;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.flowable.FlowableUtil;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.DeptApi;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/24 17:17
 */
@Service
public class ContractChangeServiceImpl implements ContractChangeService {
    private final Integer RESULT_DRAFT = 0;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private AttachmentRelMapper attachmentRelMapper;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private MyCollectModelMapper myCollectModelMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ContractCategoryMapper contractCategoryMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private FlowService flowService;
    @Resource
    private PrefAgreementRelMapper prefAgreementRelMapper;
    @Resource
    private TerminateContractMapper terminateContractMapper;
    @Resource
    private PerfTaskService perfTaskService;
    @Resource
    private DocumentRelMapper documentRelMapper;
    @Resource
    private ContractPerforMapper contractPerforMapper;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private ContractPerfService contractPerfService;
    @Resource
    private BpmContractService bpmContractService;
    @Resource
    private ContractParameterMapper contractParameterMapper;
    @Resource
    private ContractSealMapper contractSealMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractProjectPurchasingMapper contractProjectPurchasingMapper;
    @Resource
    private BpmContractMapper bpmContractMapper;
    @Resource
    private SimpleContractMapper simpleContractMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private BpmContractChangeMapper bpmContractChangeMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private BpmContractChangePaymentMapper changePaymentMapper;
    @Resource
    private ElementMapper elementMapper;
    @Resource
    private ChangeElementMapper changeElementMapper;
    @Resource
    private TaskService taskService;
    @Resource
    private BusinessFileMapper businessFileMapper;
    @Resource
    private DeptApi deptApi;
    @Autowired
    private PaymentApplicationMapper paymentApplicationMapper;


    /**
     * 保存补充协议
     */
    @Override
    public String saveSupplement(ContractChangeSaveReqVO vo) {
        checkExist(vo);
        SimpleContractDO contractDO = ContractConverter.INSTANCE.changeSave2DO(vo);
        simpleContractMapper.insert(contractDO);
        return contractDO.getId();
    }

    /**
     * 校验变更编号
     */
    private void checkExist(ContractChangeSaveReqVO vo) {
        Long count = simpleContractMapper.selectCount(new LambdaQueryWrapperX<SimpleContractDO>()
//                .eq(SimpleContractDO::getCode, vo.getCode())
        );
        if (0 < count) {
            throw exception(CODE_EXIST_ERROR);
        }
    }

    /**
     * 发起补充协议申请
     */
    @Override
    public String submitContractChangeApproveFlowable(Long loginUserId, IdReqVO reqVO) {

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
        BpmContractChangeDO bpmContractChangeDO = new BpmContractChangeDO().setContractId(contractDO.getId())
                .setContractName(contractDO.getName())
                .setReason(contractDO.getChangeReason())
                .setUserId(loginUserId)
                .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        // 2 发起申请 BPM
        // 2.1 流程变量
        Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
        processInstanceVariables.put("contractId", contractId);
        //查询合同类型，获取归档key
        String definitionKey = ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey();
        ContractDO contract = contractMapper.selectOne(new LambdaQueryWrapper<ContractDO>()
                .eq(ContractDO::getId, contractId).select(ContractDO::getContractType));
        if (ObjectUtil.isNotEmpty(contract)) {
            ContractType contractType = contractTypeMapper.selectOne(new LambdaQueryWrapperX<ContractType>()
                    .eq(ContractType::getId, contract.getContractType()));
            if (ObjectUtil.isNotEmpty(contractType) && ObjectUtil.isNotEmpty(contractType.getChangeProcess())) {
                definitionKey = contractType.getChangeProcess();
            }
        }

        // 2.2 流程实例id
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO()
                        .setProcessDefinitionKey(definitionKey)
                        .setVariables(processInstanceVariables).setBusinessKey(contractId));
        bpmContractChangeMapper.insert(bpmContractChangeDO.setProcessInstanceId(processInstanceId));
        return bpmContractChangeDO.getId();
    }

    /**
     * 合同变更-获取任务分页（0=全部 1=已审批 2=未审批）
     */
    @Override
    public BigContractChangeListApproveRespVO getBpmAllTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        // 查询所有任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractDO> doPageResult = contractMapper.selectContractChangeApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);

    }

    @Override
    public BigContractChangeListApproveRespVO getBpmDoneTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        // 查询待办任务
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        //获得已处理任务数据(已过滤掉已取消的任务),可筛选审批状态
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey(), taskResult);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractDO> doPageResult = contractMapper.selectContractChangeApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);

    }

    @Override
    public BigContractChangeListApproveRespVO getBpmToDoTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        // 查询待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractDO> doPageResult = contractMapper.selectContractChangeApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);

    }

    private BigContractChangeListApproveRespVO enhanceBpmPage(PageResult<ContractDO> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {
        List<ContractDO> doList = doPageResult.getList();
        List<ContractChangeListApproveRespVO> respVOList = ContractConverter.INSTANCE.convertBpmDO2RespList(doList);
        if (CollectionUtil.isNotEmpty(doList)) {
            //合同类型map
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);

            //发起人信息map
            List<AdminUserRespDTO> userRespDTOList = adminUserApi.getUserList();
            Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);

            //流程信息
            List<String> doIdList = doList.stream().map(ContractDO::getId).collect(Collectors.toList());
            List<BpmContractChangeDO> bpmDOList = bpmContractChangeMapper.selectList(new LambdaQueryWrapperX<BpmContractChangeDO>()
                    .inIfPresent(BpmContractChangeDO::getContractId, doIdList));
            Map<String, BpmContractChangeDO> bpmDOMap = CollectionUtils.convertMap(bpmDOList, BpmContractChangeDO::getContractId);
            List<String> instanceList = bpmDOList.stream().map(BpmContractChangeDO::getProcessInstanceId).collect(Collectors.toList());

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
            for (ContractChangeListApproveRespVO respVO : respVOList) {
//                ContractType contractType = contractTypeMap.get(respVO.getContractType());
//                if (ObjectUtil.isNotNull(contractType)) {
//                    respVO.setContractTypeName(contractType.getName());
//                }
                BpmContractChangeDO bpmDO = bpmDOMap.get(respVO.getId());
                if (ObjectUtil.isNotNull(bpmDO)) {
                    //最新审批时间
                    respVO.setApproveTime(bpmDO.getUpdateTime());
                    //流程实例
                    respVO.setProcessInstanceId(bpmDO.getProcessInstanceId());
                    //提交人
                    AdminUserRespDTO userRespDTO = userRespDTOMap.get(Long.valueOf(bpmDO.getCreator()));
                    if (ObjectUtil.isNotNull(userRespDTO)) {
                        respVO.setSubmitter(userRespDTO.getNickname());
                    }
                    //流程状态
                    respVO.setResult(bpmDO.getResult());
                    //流程任务
                    ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO = instanceRelationInfoRespDTOMap.get(bpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)) {
                        respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
                    }
                }
            }
            PageResult<ContractChangeListApproveRespVO> pageResult = new PageResult<ContractChangeListApproveRespVO>();
            pageResult.setList(respVOList).setTotal(doPageResult.getTotal());
            BigContractChangeListApproveRespVO respVO = new BigContractChangeListApproveRespVO().setPageResult(pageResult);
            //获取配置
            respVO.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE)));
            return respVO;
        }
        BigContractChangeListApproveRespVO result = new BigContractChangeListApproveRespVO()
                .setPageResult(new PageResult<ContractChangeListApproveRespVO>()
                        .setList(Collections.emptyList())
                        .setTotal(doPageResult.getTotal())
                );
        result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE)));
        return result;
    }

    /**
     * 合同变更申请列表
     */
    @Override
    public PageResult<ContractChangePageRespVO> listSubmitContractChange(ContractChangePageReqVO vo) {
        PageResult<SimpleContractDO> contractDOList = simpleContractMapper.listSubmitContractChange(vo);
        PageResult<ContractChangePageRespVO> pageResult = ContractConverter.INSTANCE.do2RespPage(contractDOList);
        return enhancePage(pageResult);

    }

    private PageResult<ContractChangePageRespVO> enhancePage(PageResult<ContractChangePageRespVO> pageResult) {
        List<ContractChangePageRespVO> list = pageResult.getList();
        if (CollectionUtil.isNotEmpty(list)) {
            List<String> idList = list.stream().map(ContractChangePageRespVO::getId).collect(Collectors.toList());
            List<BpmContractChangeDO> bpmList = bpmContractChangeMapper.selectList(new LambdaQueryWrapperX<BpmContractChangeDO>().in(BpmContractChangeDO::getContractId, idList));
            Map<String, BpmContractChangeDO> bpmMap = CollectionUtils.convertMap(bpmList, BpmContractChangeDO::getContractId);


            List<String> instanceList = bpmList.stream().map(BpmContractChangeDO::getProcessInstanceId).collect(Collectors.toList());
            //所有任务
            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            //处理过的任务
            Map<String, BpmTaskAllInfoRespVO> endTimeTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            //待处理的任务
            Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(SecurityFrameworkUtils.getLoginUserId(), instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
                toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTime(taskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
                toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }


            List<SimpleContractDO> contractDOList = simpleContractMapper.selectList();
            Map<String, SimpleContractDO> contractDOMap = CollectionUtils.convertMap(contractDOList, SimpleContractDO::getId);

            //用户信息
            List<AdminUserRespDTO> userList = adminUserApi.getUserList();
            Map<Long, AdminUserRespDTO> userMap = userList.stream().collect(Collectors.toMap(AdminUserRespDTO::getId, Function.identity()));


            for (ContractChangePageRespVO respVO : list) {
                //主合同名称
                SimpleContractDO contractDO = contractDOMap.get(respVO.getMainContractId());
                if (ObjectUtil.isNotNull(contractDO)) {
                    respVO.setMainContractName(contractDO.getName());
                }

                //流程信息
                BpmContractChangeDO bpmDO = bpmMap.get(respVO.getId());
                ContractStatusEnums enums = ContractStatusEnums.getInstance(respVO.getStatus());
                BpmProcessInstanceResultEnum bpmResultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
                if (ObjectUtil.isNotNull(bpmDO)) {
                    //流程实例id
                    respVO.setProcessInstanceId(bpmDO.getProcessInstanceId());
                    if (ContractStatusEnums.APPROVE_BACK == enums) {
                        //退回任务id
                        BpmTaskAllInfoRespVO task = taskMap.get(bpmDO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(task)) {
                            respVO.setTaskId(task.getTaskId());
                        }
                    }
                    //申请人
                    AdminUserRespDTO user = userMap.get(Long.valueOf(bpmDO.getCreator()));
                    if (ObjectUtil.isNotNull(user)) {
                        respVO.setSubmitterName(user.getNickname());
                    }
                    //待办任务的被分配人
                    if (BpmProcessInstanceResultEnum.APPROVE != bpmResultEnum) {
                        BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = toDoTaskMap.get(bpmDO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
                            respVO.setAssigneeId(bpmTaskAllInfoRespVO.getAssigneeUserId());
                        }
                    }
                }
            }
        }
        return pageResult;
    }

    @Override
    public List<ContractChangePageRespVO> listContractChangeByMainContractId(IdReqVO reqVO) {
        String mainContractId = reqVO.getId();
        List<SimpleContractDO> changeContractList = simpleContractMapper.selectList(new LambdaQueryWrapperX<SimpleContractDO>()
                .eqIfPresent(SimpleContractDO::getMainContractId, mainContractId));
        List<ContractChangePageRespVO> respVOList = ContractConverter.INSTANCE.simpleDO2RespList(changeContractList);
        return enhanceList(respVOList);
    }

    private List<ContractChangePageRespVO> enhanceList(List<ContractChangePageRespVO> respVOList) {
        if (CollectionUtil.isEmpty(respVOList)) {
            return Collections.emptyList();
        }
        List<String> idList = respVOList.stream().map(ContractChangePageRespVO::getId).collect(Collectors.toList());
        List<BpmContractChangeDO> bpmList = bpmContractChangeMapper.selectList(new LambdaQueryWrapperX<BpmContractChangeDO>().in(BpmContractChangeDO::getContractId, idList));
        Map<String, BpmContractChangeDO> bpmMap = CollectionUtils.convertMap(bpmList, BpmContractChangeDO::getContractId);


        List<AdminUserRespDTO> userRespDTOList = adminUserApi.getUserList();
        Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);
        for (ContractChangePageRespVO respVO : respVOList) {
            BpmContractChangeDO bpmDO = bpmMap.get(respVO.getId());
            if (ObjectUtil.isNotNull(bpmDO)) {
                AdminUserRespDTO submitter = userMap.get(Long.valueOf(bpmDO.getCreator()));
                respVO.setSubmitTime(bpmDO.getCreateTime());
                if (ObjectUtil.isNotNull(submitter)) {
                    respVO.setSubmitterName(submitter.getNickname());
                }
            }
        }
        return respVOList;
    }

    /**
     * 保存补充协议(暂时)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveFastSupplement(ContractChangeSaveReqVO vo) throws Exception {
        Long l = bpmContractChangeMapper.selectCount(new LambdaQueryWrapperX<BpmContractChangeDO>()
                .eq(BpmContractChangeDO::getMainContractId, vo.getMainContractId()).eq(BpmContractChangeDO::getResult, 0));
        if (l > 0) {
            throw exception(ErrorCodeConstants.SYSTEM_ERROR, "该合同已存在变动");
        }
        BpmContractChangeDO changeDO = ChangeConverter.INSTANCE.changeSave2DO(vo);
        String isContentChange = vo.getIsTermChange() + "," + vo.getIsElementChange() + "," + vo.getIsPaymentChange();
        Long count = bpmContractChangeMapper.selectCount(new LambdaQueryWrapperX<BpmContractChangeDO>().eq(BpmContractChangeDO::getMainContractId, vo.getMainContractId()));
        int sequenceNumber = (int) (count + 1);
        String formattedSequence = String.format("%02d", sequenceNumber);
        changeDO.setChangeCode("BD-" + vo.getContractCode() + "-" + formattedSequence);
        changeDO.setIsContentChange(isContentChange);
        //查询合同状态
        ContractDO contractDO = contractMapper.selectById(vo.getMainContractId());
        changeDO.setProtoStatus(contractDO.getStatus());
//        checkExistFast(changeDO);
        bpmContractChangeMapper.insert(changeDO);
        //支付计划
        if (ObjectUtil.isNotEmpty(vo.getChangePaymentList())) {
            //校验支付金额相加是否等于合同金额
            BigDecimal sum = vo.getChangePaymentList().stream().map(BpmContractChangePaymentVO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (sum.compareTo(BigDecimal.valueOf(contractDO.getAmount())) != 0) {
                throw exception(ErrorCodeConstants.SYSTEM_ERROR, "支付计划金额总和不等于合同金额");
            }
            vo.getChangePaymentList().forEach(item -> item.setChangeId(changeDO.getId()).setPaymentId(item.getId()).setId(null).setContractId(changeDO.getMainContractId()));
            changePaymentMapper.insertBatch(ChangeConverter.INSTANCE.toPaymentDOList(vo.getChangePaymentList()));
        }
        if (ObjectUtil.isNotEmpty(vo.getChangeElementList())) {
            vo.getChangeElementList().forEach(item -> item.setChangeId(changeDO.getId()));
            changeElementMapper.insertBatch(vo.getChangeElementList());
        }
        //存入附件表
        if (ObjectUtil.isNotEmpty(vo.getFiles())) {
            processAttachments(vo.getFiles(), changeDO.getId());
        }
        IdReqVO reqVO = new IdReqVO().setId(changeDO.getId());
        String processInstanceId = this.submitContractChangeApproveFlowableFast(getLoginUserId(), reqVO);
        //修改合同状态为合同变更
        contractMapper.updateById(new ContractDO().setId(changeDO.getMainContractId()).setStatus(ContractStatusEnums.CONTRACT_CHANGE.getCode()));
        if (ObjectUtil.isNotEmpty(vo.getIsSubmit()) && IfNumEnums.YES.getCode().equals(vo.getIsSubmit())) {
            //添加taskId
            if (ObjectUtil.isNotEmpty(processInstanceId)) {
                List<String> processInstanceIds = Arrays.asList(processInstanceId);
                List<BpmTaskAllInfoRespVO> instanceIds = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(getLoginUserId(), processInstanceIds);
                if (ObjectUtil.isNotEmpty(instanceIds)) {
                    for (BpmTaskAllInfoRespVO bpmTaskRespDTO : instanceIds) {
                        BpmTaskApproveReqVO taskApproveReqVO = new BpmTaskApproveReqVO().setTaskId(bpmTaskRespDTO.getTaskId()).setReason(vo.getAdvice());
                        taskService.approveTask(getLoginUserId(), taskApproveReqVO);
                    }

                }
            }
        }
        return changeDO.getId();
    }

    private void processAttachments(List<AttachmentVO> attachments, String businessId) {
        List<BusinessFileDO> businessFileDOS = attachments.stream()
                .map(item -> {
                    BusinessFileDO businessFileDO = new BusinessFileDO();
                    businessFileDO.setBusinessId(businessId)
                            .setFileId(item.getFileId())
                            .setFileName(item.getName());
                    return businessFileDO;
                })
                .collect(Collectors.toList());

        businessFileMapper.insertBatch(businessFileDOS);
    }

    /**
     * 校验编号是否重复(暂时)
     */
    private void checkExistFast(BpmContractChangeDO changeDO) {
        Long count = bpmContractChangeMapper.selectCount(new LambdaQueryWrapperX<BpmContractChangeDO>()
                .eq(BpmContractChangeDO::getChangeCode, changeDO.getChangeCode())
        );
        if (0 < count) {
            throw exception(CODE_EXIST_ERROR);
        }

    }

    /**
     * 校验必填字段
     */
    private void checkMustInput(ContractChangeSaveReqVO vo) {
        if (StringUtils.isEmpty(vo.getName())) {
            throw exception(DIY_ERROR, "合同变动名称不能为空");
        }
        if (StringUtils.isEmpty(vo.getReason())) {
            throw exception(DIY_ERROR, "申请事由不能为空");
        }
        if (ObjectUtil.isEmpty(vo.getFileAddId())) {
            throw exception(DIY_ERROR, "合同文件不能为空");
        }
        if (IfNumEnums.YES.getCode().equals(vo.getIsTermChange()) && ObjectUtil.isEmpty(vo.getOriginalClauseText())) {
            if (!ContractChangeTypeEnums.SUPPLEMENT.getCode().equals(vo.getChangeType())){
                throw exception(DIY_ERROR, "条款文本变更前内容不能为空");
            }
        }
        if (IfNumEnums.YES.getCode().equals(vo.getIsTermChange()) && ObjectUtil.isEmpty(vo.getUpdatedClauseText())) {
            throw exception(DIY_ERROR, "条款文本变更后内容不能为空");
        }
        if (IfNumEnums.YES.getCode().equals(vo.getIsElementChange()) && CollectionUtil.isEmpty(vo.getChangeElementList())) {
            throw exception(DIY_ERROR, "表单数据变更项不能为空");
        }
        if (IfNumEnums.YES.getCode().equals(vo.getIsPaymentChange()) && CollectionUtil.isEmpty(vo.getChangePaymentList())) {
            throw exception(DIY_ERROR, "履约计划变更项不能为空");
        }
    }

    /**
     * 发起补充协议申请(暂时)
     */
    @Override
    public String submitContractChangeApproveFlowableFast(Long loginUserId, IdReqVO reqVO) {

        String changeId = reqVO.getId();
        BpmContractChangeDO changeDO = bpmContractChangeMapper.selectOne(new LambdaQueryWrapperX<BpmContractChangeDO>().eq(BpmContractChangeDO::getId, changeId));

        if (ObjectUtil.isNull(changeDO)) {
            throw exception(EMPTY_DATA_ERROR);
        }
        //1.插入请求单
        //校验主合同是否有变动协议还在审批中
//        List<BpmContractChangeDO> changeDOList = bpmContractChangeMapper.selectList(new LambdaQueryWrapperX<BpmContractChangeDO>().eqIfPresent(BpmContractChangeDO::getMainContractId, changeDO.getMainContractId()));
//        if (CollectionUtil.isNotEmpty(changeDOList)) {
//            for (BpmContractChangeDO change : changeDOList) {
//                BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(change.getResult());
//                if (BpmProcessInstanceResultEnum.PROCESS == resultEnum || BpmProcessInstanceResultEnum.BACK == resultEnum) {
//                    throw exception(CONTRACT_CHANGE_APPROVING);
//                }
//            }
//        }


        BpmContractChangeDO bpmContractChangeDO = new BpmContractChangeDO()
                .setId(changeId)
                .setUserId(changeDO.getUserId())
                .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        // 2 发起申请 BPM
        // 2.1 流程变量
        Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
        processInstanceVariables.put("changeId", changeId);
        if (Objects.equals(changeDO.getChangeType(), ContractChangeTypeEnums.CHANGE.getCode()) || Objects.equals(changeDO.getChangeType(), ContractChangeTypeEnums.SUPPLEMENT.getCode()) || Objects.equals(changeDO.getChangeType(), ContractChangeTypeEnums.TERMINATE.getCode()) || Objects.equals(changeDO.getChangeType(), ContractChangeTypeEnums.CANCELLATION.getCode())) {
            processInstanceVariables.put("passChangeType", 1);
        } else {
            processInstanceVariables.put("passChangeType", 2);
        }
        // 2.2 流程实例id
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO()
                        .setProcessDefinitionKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey())
                        .setVariables(processInstanceVariables).setBusinessKey(changeId));
        bpmContractChangeMapper.updateById(bpmContractChangeDO.setProcessInstanceId(processInstanceId));
        return processInstanceId;
    }

    /**
     * 合同变更-申请列表(暂时)
     */
    @Override
    public PageResult<ContractChangePageRespVO> listSubmitContractChangeFast(ContractChangePageReqVO reqVO) {
        PageResult<BpmContractChangeDO> doList = bpmContractChangeMapper.listSubmitContractChangeFast(reqVO);
        PageResult<ContractChangePageRespVO> pageResult = ChangeConverter.INSTANCE.do2RespPage(doList);
        return enhanceFastPage(pageResult);
    }

    private PageResult<ContractChangePageRespVO> enhanceFastPage(PageResult<ContractChangePageRespVO> pageResult) {
        List<ContractChangePageRespVO> list = pageResult.getList();
        if (CollectionUtil.isNotEmpty(list)) {
            List<String> idList = list.stream().map(ContractChangePageRespVO::getId).collect(Collectors.toList());
            List<BpmContractChangeDO> bpmList = bpmContractChangeMapper.selectList(new LambdaQueryWrapperX<BpmContractChangeDO>().in(BpmContractChangeDO::getId, idList));
            Map<String, BpmContractChangeDO> bpmMap = CollectionUtils.convertMap(bpmList, BpmContractChangeDO::getContractId);

            List<String> instanceList = bpmList.stream()
                    .filter(bpm -> bpm.getProcessInstanceId() != null)
                    .map(BpmContractChangeDO::getProcessInstanceId)
                    .collect(Collectors.toList());
            //根据流程实例id分组流程任务
            List<SimpleTaskDTO> untreatedTaskDTOList = bpmTaskApi.getNextUntreatedTaskInfoByProcessIds(instanceList);
//            Map<String, List<SimpleTaskDTO>> tasksByProcessInstanceIdMap = taskDTOList.stream().collect(Collectors.groupingBy(SimpleTaskDTO::getProcessInstanceId));
//            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, SimpleTaskDTO> untreatedTaskDTOMap = CollectionUtils.convertMap(untreatedTaskDTOList, SimpleTaskDTO::getProcessInstanceId);
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(SecurityFrameworkUtils.getLoginUserId(), instanceList);
            //待处理的任务
            List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(SecurityFrameworkUtils.getLoginUserId(), instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTime(taskAllInfoRespVOList);
                toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }

            //根据流程实例id分组未处理任务


            List<SimpleContractDO> contractDOList = simpleContractMapper.selectList();
            Map<String, SimpleContractDO> contractDOMap = CollectionUtils.convertMap(contractDOList, SimpleContractDO::getId);

            //用户信息
            List<AdminUserRespDTO> userList = adminUserApi.getUserList();
            Map<Long, AdminUserRespDTO> userMap = userList.stream().collect(Collectors.toMap(AdminUserRespDTO::getId, Function.identity()));
            Long userId = getLoginUserId();

            for (ContractChangePageRespVO respVO : list) {
                //默认不可撤回
                respVO.setEnableRepeal(false);
                //主合同名称
                SimpleContractDO contractDO = contractDOMap.get(respVO.getMainContractId());
                if (ObjectUtil.isNotNull(contractDO)) {
                    respVO.setMainContractName(contractDO.getName());
                }

                //流程信息

                if (ObjectUtil.isNotNull(respVO.getProcessInstanceId())) {
                    //流程实例id
                    if (Objects.equals(BpmProcessInstanceResultEnum.BACK.getResult(), respVO.getResult())) {
                        //退回任务id
                        BpmTaskAllInfoRespVO task = taskMap.get(respVO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(task)) {
                            respVO.setTaskId(task.getTaskId());
                        }
                    }
                    //判断是否可以撤回。
                    SimpleTaskDTO taskDTO = untreatedTaskDTOMap.get(respVO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(taskDTO)) {
                        FlowableModelEnums flowableModelEnums = FlowableModelEnums.getInstanceByName(taskDTO.getName());
                        if (FlowableModelEnums.FIRST_LEVEL == flowableModelEnums) {
                            respVO.setEnableRepeal(true);
                        }
                    }
                }
                //申请人
                AdminUserRespDTO user = userMap.get(Long.valueOf(respVO.getCreator()));
                if (ObjectUtil.isNotNull(user)) {
                    respVO.setSubmitterName(user.getNickname());
                }
                //待办任务的被分配人
                BpmProcessInstanceResultEnum bpmResultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
                if (BpmProcessInstanceResultEnum.APPROVE != bpmResultEnum) {
                    BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = toDoTaskMap.get(respVO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
                        Long assigneeId = bpmTaskAllInfoRespVO.getAssigneeUserId();
                        respVO.setAssigneeId(assigneeId);
                        //如果处理人不是发起人，则不可以撤回
                        if (!userId.equals(assigneeId)) {
                            respVO.setIfRepeal(IfEnums.NO.getCode());
                        } else {
                            respVO.setIfRepeal(IfEnums.YES.getCode());
                        }
                    }
                }
            }
        } else {
            pageResult.setTotal(pageResult.getTotal()).setList(Collections.emptyList());
        }
        return pageResult;
    }

    /**
     * 合同变更-获取任务分页（0=全部 1=已审批 2=未审批）
     */
    @Override
    public BigContractChangeListApproveRespVO getBpmAllTaskPageFast(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        //查询合同类型表，取变更流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().eq(ContractType::getTypeStatus, 1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getChangeProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        // 查询所有任务
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(definitionKeys.get(0), definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
        } else {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey());
        }
        //去除已取消的任务。
        processInstanceRelationInfoRespDTOList = EcontractUtil.clearRepealTask(processInstanceRelationInfoRespDTOList);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<BpmContractChangeDO> doPageResult = bpmContractChangeMapper.selectContractChangeApprovePage(pageVO);
        BigContractChangeListApproveRespVO bigContractChangeListApproveRespVO = enhanceBpmPageFast(doPageResult, instanceRelationInfoRespDTOMap);
        extracted(bigContractChangeListApproveRespVO);
        return bigContractChangeListApproveRespVO;

    }


    /**
     * 合同管理 - 合同关闭
     * 变更类型为   -关闭、取消、作废-
     *
     * @param contractPageReqVO
     * @return
     */
    @Override
    public PageResult<ContractChangePageRespVO> getAutoBpmOverTaskPageFast(ContractChangePageReqVO contractPageReqVO) {
        switch (contractPageReqVO.getFlag()) {
            case 1:
                contractPageReqVO.setChangeType(ContractChangeTypeEnums.TERMINATED.getCode());
                break;
            case 2:
                if (ObjectUtil.isEmpty(contractPageReqVO.getChangeType())) {
                    contractPageReqVO.setChangeTypes(CollectionUtil.newArrayList(ContractChangeTypeEnums.CANCEL.getCode(), ContractChangeTypeEnums.CANCELLATION.getCode()));
                }
                if (ObjectUtil.isEmpty(contractPageReqVO.getFrontCode())) {
                    contractPageReqVO.setFrontCodes(CollectionUtil.newArrayList(CommonFlowableReqVOResultStatusEnums.APPROVING.getResultCode(), CommonFlowableReqVOResultStatusEnums.SUCCESS.getResultCode()));
                }
                break;
        }
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getStatus())) {
            //查询合同表状态为履约完成的合同 待关闭状态
            if (contractPageReqVO.getStatus().equals(ContractStatusEnums.PERFORMANCE_COMPLETE.getCode())) {
                PageResult<ContractDO> contractDOPageResult = contractMapper.selectPage(contractPageReqVO, new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getStatus, ContractStatusEnums.PERFORMANCE_COMPLETE.getCode()));
                PageResult<ContractChangePageRespVO> result = ContractConverter.INSTANCE.converPageRespVO(contractDOPageResult);
                return result;
            }
            //已关闭
            if (contractPageReqVO.getStatus().equals(ContractStatusEnums.PERFORMANCE_CLOSURE.getCode())) {
                PageResult<ContractDO> contractDOPageResult = contractMapper.selectPage(contractPageReqVO, new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getStatus, ContractStatusEnums.PERFORMANCE_CLOSURE.getCode()));
                PageResult<ContractChangePageRespVO> result = ContractConverter.INSTANCE.converPageRespVO(contractDOPageResult);
                return result;
            }

        }
        PageResult<BpmContractChangeDO> contractDOList = bpmContractChangeMapper.listOverContractChange(contractPageReqVO);
        PageResult<ContractChangePageRespVO> pageResult = ChangeConverter.INSTANCE.do2RespPage(contractDOList);
        //获取合同id
        List<String> contractIds = pageResult.getList().stream().map(ContractChangePageRespVO::getMainContractId).collect(Collectors.toList());
        List<ContractDO> contractDOS = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(contractIds)) {
            contractDOS = contractMapper.selectList(ContractDO::getId, contractIds);
        }

        Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
        //获取申请人
        List<Long> creatorIds = contractDOList.getList().stream().map(BpmContractChangeDO::getCreator).map(Long::parseLong).collect(Collectors.toList());
        List<AdminUserRespDTO> userList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(creatorIds)) {
            userList = adminUserApi.getUserList(creatorIds);
        }
        Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(userList, AdminUserRespDTO::getId);
        pageResult.getList().forEach(respVO -> {
            ContractDO contractDO = contractDOMap.get(respVO.getMainContractId());
            if (ObjectUtil.isNotEmpty(contractDO)) {
                respVO.setStatus(contractDO.getStatus());
                respVO.setStatusName((ContractStatusEnums.getInstance(contractDO.getStatus())).getDesc());
            }
            AdminUserRespDTO user = userMap.get(Long.valueOf(respVO.getCreator()));
            if (ObjectUtil.isNotEmpty(user)) {
                respVO.setSubmitterName(user.getNickname());
            }
            respVO.setResultName(BpmProcessInstanceResultEnum.getInstance(respVO.getResult()).getDesc());
            respVO.setChangeTypeName(ContractChangeTypeEnums.getInstance(respVO.getChangeType()).getInfo());
        });
        return enhancePage(pageResult);
    }

    @Override
    public BigContractChangeListApproveRespVO getBpmDoneTaskPageFast(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        //获得已处理任务数据
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey(), taskResult);
        //去除已取消的任务。
        processInstanceRelationInfoRespDTOList = EcontractUtil.clearRepealTask(processInstanceRelationInfoRespDTOList);
        //去除待处理列表中的任务
        List<ContractProcessInstanceRelationInfoRespDTO> toDoProcessInstanceRelationList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey());
        List<String> toDoInstanceIdList = toDoProcessInstanceRelationList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());


        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        instanceIdList.removeAll(toDoInstanceIdList); // 排除待办任务
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<BpmContractChangeDO> doPageResult = bpmContractChangeMapper.selectContractChangeApprovePage(pageVO);
        BigContractChangeListApproveRespVO bigContractChangeListApproveRespVO = enhanceBpmPageFast(doPageResult, instanceRelationInfoRespDTOMap);
        extracted(bigContractChangeListApproveRespVO);
        return bigContractChangeListApproveRespVO;

    }

    private void extracted(BigContractChangeListApproveRespVO bigContractChangeListApproveRespVO) {
        for (ContractChangeListApproveRespVO respVO : bigContractChangeListApproveRespVO.getPageResult().getList()) {
            if (ObjectUtil.isNotEmpty(respVO)) {
                if (respVO.getStatus() != null) {
                    respVO.setStatus(respVO.getStatus());
                    ContractStatusEnums statusEnum = ContractStatusEnums.getInstance(respVO.getStatus());
                    if (statusEnum != null) {
                        respVO.setStatusName(statusEnum.getDesc());
                    }
                }
                if (respVO.getDoneTaskResult() != null) {
                    BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getDoneTaskResult());
                    if (resultEnum != null) {
                        respVO.setResultName(resultEnum.getDesc());
                    }
                }
                if (respVO.getResult() != null) {
                    BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
                    if (resultEnum != null) {
                        respVO.setResultStatusName(resultEnum.getDesc());
                    }
                }
                if (respVO.getChangeType() != null) {
                    ContractChangeTypeEnums changeTypeEnum = ContractChangeTypeEnums.getInstance(respVO.getChangeType());
                    if (changeTypeEnum != null) {
                        respVO.setChangeTypeName(changeTypeEnum.getInfo());
                    }
                }
            }
        }
    }

    @Override
    public BigContractChangeListApproveRespVO getBpmToDoTaskPageFast(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        // 查询待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<BpmContractChangeDO> doPageResult = bpmContractChangeMapper.selectContractChangeApprovePage(pageVO);
        BigContractChangeListApproveRespVO bigContractChangeListApproveRespVO = enhanceBpmPageFast(doPageResult, instanceRelationInfoRespDTOMap);
        extracted(bigContractChangeListApproveRespVO);
        return bigContractChangeListApproveRespVO;
    }

    private BigContractChangeListApproveRespVO enhanceBpmPageFast(PageResult<BpmContractChangeDO> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {
        List<BpmContractChangeDO> doList = doPageResult.getList();
        List<ContractChangeListApproveRespVO> respVOList = ChangeConverter.INSTANCE.convertBpmDO2RespList(doList);
        if (CollectionUtil.isNotEmpty(doList)) {
            List<String> contractId = doList.stream().map(BpmContractChangeDO::getMainContractId).collect(Collectors.toList());
            List<ContractDO> contractDOS = contractMapper.selectList(ContractDO::getId, contractId);
            Map<String, ContractDO> contractMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
            //合同类型map
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);

            //发起人信息map
            List<AdminUserRespDTO> userRespDTOList = adminUserApi.getUserList();
            Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);

            //流程信息（业务和流程同表）
            List<String> doIdList = doList.stream().map(BpmContractChangeDO::getId).collect(Collectors.toList());
            Map<String, BpmContractChangeDO> bpmDOMap = CollectionUtils.convertMap(doList, BpmContractChangeDO::getId);
            List<String> instanceList = doList.stream().map(BpmContractChangeDO::getProcessInstanceId).collect(Collectors.toList());

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
            Map<String, BpmTaskAllInfoRespVO> allTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> allInfoRespVOList;
            if (CollectionUtil.isNotEmpty(instanceList)) {
                originalTaskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(originalTaskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTimeByUserId(originalTaskAllInfoRespVOList, getLoginUserId());
                toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                taskDoneEndTimeAllInfoRespVOList = EcontractUtil.distinctDoneTaskLatestEndTime(originalTaskAllInfoRespVOList);
                taskDoneEndTimeMap = CollectionUtils.convertMap(taskDoneEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
                //获取下一个节点的任务
                allInfoRespVOList = bpmTaskApi.getAllTaskIdByProcessInstanceIds(instanceList);
                allInfoRespVOList = EcontractUtil.distinctTask(allInfoRespVOList);
                allTaskMap = CollectionUtils.convertMap(allInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

            }

            //有结束时间的流程任务
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskEndTimeAllInfoRespVOList = EcontractUtil.distinctTaskHaveEndTime(originalTaskAllInfoRespVOList);
                taskEndTimeMap = CollectionUtils.convertMap(taskEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }
            for (ContractChangeListApproveRespVO respVO : respVOList) {

                //合同状态
                if (ObjectUtil.isNotEmpty(contractMap.get(respVO.getMainContractId()))) {
                    respVO.setStatus(contractMap.get(respVO.getMainContractId()).getStatus());
                    respVO.setContractName(contractMap.get(respVO.getMainContractId()).getName());
                    respVO.setContractCode(contractMap.get(respVO.getMainContractId()).getCode());
                    respVO.setProtoStatusName(ContractStatusEnums.getInstance(respVO.getProtoStatus()).getDesc());
                    respVO.setContractType(contractMap.get(respVO.getMainContractId()).getContractType());
                    //设置合同类型
                    if (ObjectUtil.isNotEmpty(contractTypeMap)) {
                        String contractTypeId = contractMap.get(respVO.getMainContractId()).getContractType();
                        ContractType contractType = contractTypeId == null ? null : contractTypeMap.get(contractTypeId);
                        respVO.setContractTypeName(contractType == null ? null : contractType.getName());
                    }
                }
                //最新审批时间
                respVO.setApproveTime(respVO.getUpdateTime());

                //流程实例
                respVO.setProcessInstanceId(respVO.getProcessInstanceId());
                //提交人
                AdminUserRespDTO userRespDTO = userRespDTOMap.get(Long.valueOf(respVO.getCreator()));
                if (ObjectUtil.isNotNull(userRespDTO)) {
                    respVO.setSubmitter(userRespDTO.getNickname());
                }
                //流程状态
                respVO.setResult(respVO.getResult());
                //流程任务
                ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO = instanceRelationInfoRespDTOMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)) {
                    respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
                }
                //历史任务信息（所有审批人）
                BpmTaskAllInfoRespVO historyTask = taskEndTimeMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(historyTask)) {
                    respVO.setHisTaskResult(historyTask.getResult());
                    respVO.setNodeName(historyTask.getName());
                } else {
                    BpmTaskAllInfoRespVO endTimeTask = taskDoneEndTimeMap.get(respVO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(endTimeTask)) {
                        respVO.setNodeName(endTimeTask.getName());
                    }
                }

                //全部审批列表的审批状态(找不到当前登录人的待办任务，既是已办任务)
                BpmTaskAllInfoRespVO toDoTask = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(toDoTask)) {
                    respVO.setAssigneeId(toDoTask.getAssigneeUserId());
                    respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTask.getAssigneeUserId()));
                    respVO.setFlowableStatusName(FlowableStatusEnums.getInstance(respVO.getFlowableStatus()).getInfo());
                    respVO.setNodeName(toDoTask.getName());
                }
                //审批状态(全部里)
                else {
                    respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
                    respVO.setFlowableStatusName(FlowableStatusEnums.getInstance(respVO.getFlowableStatus()).getInfo());
                }

                if (ObjectUtil.isNotNull(respVO.getProcessInstanceId()) && ObjectUtil.isNotNull(allTaskMap.get(respVO.getProcessInstanceId()))) {
                    respVO.setNodeName(allTaskMap.get(respVO.getProcessInstanceId()).getName());
                }
                //已审批任务的状态赋值（当前人对该审批的最近的一次操作结果）
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = instanceRelationInfoRespDTOMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setDoneTaskResult(infoRespDTO.getProcessResult());
                }
            }
            PageResult<ContractChangeListApproveRespVO> pageResult = new PageResult<ContractChangeListApproveRespVO>();
            pageResult.setList(respVOList).setTotal(doPageResult.getTotal());
            BigContractChangeListApproveRespVO respVO = new BigContractChangeListApproveRespVO().setPageResult(pageResult);
            //获取配置
            respVO.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE)));
            return respVO;
        }
        BigContractChangeListApproveRespVO result = new BigContractChangeListApproveRespVO()
                .setPageResult(new PageResult<ContractChangeListApproveRespVO>()
                        .setList(Collections.emptyList())
                        .setTotal(doPageResult.getTotal())
                );
        result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE)));
        return result;
    }

    /**
     * 根据合同id获得相关变动合同列表（弹窗）（暂时）
     */
    @Override
    public List<ContractChangePageRespVO> listContractChangeByMainContractIdFast(IdReqVO reqVO) {
        String mainContractId = reqVO.getId();
        List<BpmContractChangeDO> changeContractList = bpmContractChangeMapper.selectList(new LambdaQueryWrapperX<BpmContractChangeDO>()
                .eqIfPresent(BpmContractChangeDO::getMainContractId, mainContractId).orderByDesc(BpmContractChangeDO::getUpdateTime));
        List<ContractChangePageRespVO> respVOList = ChangeConverter.INSTANCE.simpleDO2RespList(changeContractList);
        return enhanceListFast(respVOList);
    }

    private List<ContractChangePageRespVO> enhanceListFast(List<ContractChangePageRespVO> respVOList) {
        if (CollectionUtil.isEmpty(respVOList)) {
            return Collections.emptyList();
        }
        List<String> idList = respVOList.stream().map(ContractChangePageRespVO::getId).collect(Collectors.toList());
        List<AdminUserRespDTO> userRespDTOList = adminUserApi.getUserList();
        Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);
        for (ContractChangePageRespVO respVO : respVOList) {
            AdminUserRespDTO submitter = userMap.get(Long.valueOf(respVO.getCreator()));
            respVO.setSubmitTime(respVO.getCreateTime());
            if (ObjectUtil.isNotNull(submitter)) {
                respVO.setSubmitterName(submitter.getNickname());
            }
            respVO.setChangeTypeName(ContractChangeTypeEnums.getInstance(respVO.getChangeType()).getInfo());
            respVO.setResultName(BpmProcessInstanceResultEnum.getInstance(respVO.getResult()).getDesc());
        }

        return respVOList;
    }

    /**
     * fast编辑补充协议(暂时)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateFastSupplement(ContractChangeSaveReqVO vo) {
        // 当数据为提交前的保存时，做数据必填校验
        if (IfNumEnums.YES.getCode().equals(vo.getIsSubmit())) {
            checkMustInput(vo);
        }
        BpmContractChangeDO changeDO = ChangeConverter.INSTANCE.changeSave2DO(vo);
        String isContentChange = vo.getIsTermChange() + "," + vo.getIsElementChange() + "," + vo.getIsPaymentChange();
        changeDO.setIsContentChange(isContentChange);
        bpmContractChangeMapper.updateById(changeDO);
        ContractDO contractDO = contractMapper.selectById(vo.getMainContractId());
        if (ObjectUtil.isEmpty(contractDO)) {
            throw exception(ErrorCodeConstants.CONTRACT_NAME_NOT_EXISTS);
        }
        //支付计划
        if (ObjectUtil.isNotEmpty(vo.getChangePaymentList())) {
            //校验支付金额相加是否等于合同金额
            BigDecimal sum = vo.getChangePaymentList().stream().map(BpmContractChangePaymentVO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (sum.compareTo(BigDecimal.valueOf(contractDO.getAmount())) != 0) {
                throw exception(ErrorCodeConstants.SYSTEM_ERROR, "支付计划金额总和不等于合同金额");
            }
            vo.getChangePaymentList().forEach(item -> item.setChangeId(changeDO.getId()).setPaymentId(item.getId()).setContractId(changeDO.getMainContractId()));
            changePaymentMapper.updateBatch(ChangeConverter.INSTANCE.toPaymentDOList(vo.getChangePaymentList()));
        }
        if (ObjectUtil.isNotEmpty(vo.getChangeElementList())) {
            vo.getChangeElementList().forEach(item -> item.setChangeId(changeDO.getId()));
            changeElementMapper.insertBatch(vo.getChangeElementList());
        }
        if (ObjectUtil.isNotEmpty(vo.getChangeElementList())) {
            changeElementMapper.delete(ChangeElementDO::getChangeId, changeDO.getId());
            vo.getChangeElementList().forEach(item -> item.setChangeId(changeDO.getId()).setId(null));
            changeElementMapper.insertBatch(vo.getChangeElementList());
        }
        //存入附件表
        if (ObjectUtil.isNotEmpty(vo.getFiles())) {
            processAttachments(vo.getFiles(), changeDO.getId());
        }
        return changeDO.getId();
    }

    /**
     * 校验协议是否处于草稿状态（仅草稿状态才可编辑）
     */
    private void checkModified(String changeId) {
        List<BpmContractChangeDO> contractChangeDOList = bpmContractChangeMapper.selectList(new LambdaQueryWrapperX<BpmContractChangeDO>()
                .eqIfPresent(BpmContractChangeDO::getId, changeId)
        );
        if (CollectionUtil.isEmpty(contractChangeDOList)) {
            throw exception(EMPTY_DATA_ERROR);
        }
        //校验是否可编辑（仅 草稿 和 被退回 状态才可编辑）
        int result = contractChangeDOList.get(0).getResult();
        if (RESULT_DRAFT != result && !BpmProcessInstanceResultEnum.BACK.getResult().equals(result)) {
            throw exception(CONTRACT_CHANGE_UPDATE_ERROR);
        }

    }

    /**
     * fast删除补充协议(暂时)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteFastSupplement(IdReqVO vo) {
        String changeId = vo.getId();
        if (StringUtils.isEmpty(changeId)) {
            throw exception(EMPTY_DATA_ERROR);
        }
//        checkModified(changeId);
        BpmContractChangeDO bpmContractChangeDO = bpmContractChangeMapper.selectById(changeId);
        //将合同恢复原来的状态
        contractMapper.updateById(new ContractDO().setId(bpmContractChangeDO.getMainContractId()).setStatus(bpmContractChangeDO.getProtoStatus()));
        bpmContractChangeMapper.deleteById(changeId);
        changePaymentMapper.delete(new LambdaQueryWrapperX<BpmContractChangePaymentDO>().eq(BpmContractChangePaymentDO::getChangeId, changeId));
        changeElementMapper.delete(ChangeElementDO::getChangeId, changeId);
        return "success";
    }

    @Override
    public ContractChangeOneRespVO getContractChangeById(IdReqVO vo) throws Exception {
        ContractChangeOneRespVO result = new ContractChangeOneRespVO();
        String id = vo.getId();
        BpmContractChangeDO changeDO = bpmContractChangeMapper.selectById(id);
        if (ObjectUtil.isNotNull(changeDO)) {
            result = ContractConverter.INSTANCE.changeDO2Resp(changeDO);
            if (changeDO.getIsContentChange() != null && !changeDO.getIsContentChange().contains("null")) {
                String[] split = changeDO.getIsContentChange().split(",");
                if (split.length >= 3) {
                    result.setIsTermChange(Integer.valueOf(split[0]));
                    result.setIsElementChange(Integer.valueOf(split[1]));
                    result.setIsPaymentChange(Integer.valueOf(split[2]));
                }
            } else {
                // 提供默认值
                result.setIsTermChange(0);
                result.setIsElementChange(0);
                result.setIsPaymentChange(0);
            }
            //合同基本信息
            ContractDO contract = contractMapper.selectById(changeDO.getMainContractId());
            if (ObjectUtil.isNotEmpty(contract)) {
                result.setMainContractName(contract.getName());
                result.setMainContractCode(contract.getCode());
                result.setAmount(BigDecimal.valueOf(contract.getAmount()));
                result.setStatus(contract.getStatus());
                result.setStatusName(ContractStatusEnums.getInstance(contract.getStatus()).getDesc());
            }
            result.setChangeTypeName(ContractChangeTypeEnums.getInstance(result.getChangeType()).getInfo());
            List<AdminUserRespDTO> userRespDTOList = adminUserApi.getUserList();
            Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);
            AdminUserRespDTO user = userMap.get(Long.valueOf(result.getCreator()));
            if (ObjectUtil.isNotNull(user)) {
                result.setSubmitterName(user.getNickname());
            }
            List<BpmContractChangePaymentDO> bpmContractChangePaymentDOS = changePaymentMapper.selectList(new LambdaQueryWrapperX<BpmContractChangePaymentDO>()
                    .eqIfPresent(BpmContractChangePaymentDO::getChangeId, id).orderByAsc(BpmContractChangePaymentDO::getSort));
            if (ObjectUtil.isNotEmpty(bpmContractChangePaymentDOS)) {
                List<PaymentScheduleRespVO> respVOList = PaymentScheduleConverter.INSTANCE.toRespVOList2(bpmContractChangePaymentDOS);
                fillData(respVOList);
                result.setChangePaymentList(respVOList);
            }
            //添加原合同付款计划信息
            List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                    .eq(PaymentScheduleDO::getContractId, changeDO.getMainContractId()).orderByAsc(PaymentScheduleDO::getSort));
            if (CollectionUtil.isNotEmpty(paymentScheduleDOList)) {
                List<PaymentScheduleRespVO> respVOList = PaymentScheduleConverter.INSTANCE.toRespVOList(paymentScheduleDOList);
                fillData(respVOList);
                result.setPaymentScheduleDOList(respVOList);
            }

            List<ChangeElementDO> changeElementDOS = changeElementMapper.selectList(ChangeElementDO::getChangeId, id);
            if (ObjectUtil.isNotEmpty(changeElementDOS)) {
                List<String> elementIdList = changeElementDOS.stream().map(ChangeElementDO::getElementId).collect(Collectors.toList());
                List<ElementDO> elementDOS = elementMapper.selectList(ElementDO::getId, elementIdList);
                Map<String, ElementDO> elementMap = CollectionUtils.convertMap(elementDOS, ElementDO::getId);
                List<ChangeElementVO> changeElementList = ChangeConverter.INSTANCE.toChangeElementList(changeElementDOS);
                changeElementList.forEach(item -> {
                    item.setElementName(elementMap.get(item.getElementId()).getElementName());
                });
                result.setChangeElementList(changeElementList);
            }
            if (ObjectUtil.isNotEmpty(result.getFileAddId())) {
                String name = fileApi.getName(result.getFileAddId());
                result.setFileAddName(name);
            }
            List<BusinessFileDO> contractContentFileDOS = businessFileMapper.selectByBusinessId(id);
            if (ObjectUtil.isNotEmpty(contractContentFileDOS)) {
                List<AttachmentVO> files = convertToAttachmentVOList(contractContentFileDOS);
                result.setFiles(files);
            }
            //添加taskId
            if (ObjectUtil.isNotEmpty(changeDO.getProcessInstanceId())) {
                List<String> processInstanceIds = Arrays.asList(changeDO.getProcessInstanceId());
                List<BpmTaskAllInfoRespVO> instanceIds = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(getLoginUserId(), processInstanceIds);
                if (ObjectUtil.isNotEmpty(instanceIds)) {
                    for (BpmTaskAllInfoRespVO bpmTaskRespDTO : instanceIds) {
                        result.setTaskId(bpmTaskRespDTO.getTaskId());
                    }

                }
            }
            //添加部门信息
            result.setDeptId(changeDO.getDeptId());
            DeptRespDTO dept = deptApi.getDept(changeDO.getDeptId());
            if (ObjectUtil.isNotEmpty(dept)) {
                result.setDeptName(dept.getName());
            }


        }
        return result;
    }

    private List<AttachmentVO> convertToAttachmentVOList(List<BusinessFileDO> businessFileDOS) {
        List<AttachmentVO> attachmentList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(businessFileDOS)) {
            businessFileDOS.forEach(item -> {
                attachmentList.add(new AttachmentVO()
                        .setFileId(item.getFileId())
                        .setName(item.getFileName())
                        .setIsAdd(0));
            });
        }
        return attachmentList;
    }

    private void fillData(List<PaymentScheduleRespVO> respVOList) {
        for (PaymentScheduleRespVO respVO : respVOList) {
            if (ObjectUtil.isNotEmpty(respVO.getId())) {
                Relative relative = relativeMapper.selectById(respVO.getId());
                if (ObjectUtil.isNotEmpty(relative)) {
                    if (ObjectUtil.isNotEmpty(relative.getCompanyName())) {
                        respVO.setPayee(relative.getCompanyName());
                    }
                }
                respVO.setStatusName(PaymentScheduleStatusEnums.getInstance(respVO.getStatus()).getInfo());
                respVO.setMoneyTypeName(MoneyTypeEnums.getInstance(respVO.getMoneyType()).getInfo());
            }
        }
    }

    @Override
    public String saveAndSubmitFastSupplement(ContractChangeSaveReqVO vo) throws Exception {
        String id = saveFastSupplement(vo);
        if (StringUtils.isBlank(id)) {
            throw exception(CONTRACT_CHANGE_SAVE_SUBMIT_ERROR);
        }
        IdReqVO reqVO = new IdReqVO().setId(id);
        return submitContractChangeApproveFlowableFast(getLoginUserId(), reqVO);
    }

    @Override
    public String updateAndSubmitFastSupplement(ContractChangeSaveReqVO vo) {
        updateFastSupplement(vo);
        if (StringUtils.isNotBlank(vo.getId())) {
            submitContractChangeApproveFlowableFast(getLoginUserId(), new IdReqVO().setId(vo.getId()));
            return vo.getId();
        }
        throw exception(CONTRACT_CHANGE_UPDATE_SUBMIT_ERROR);
    }

    @Override
    public PageResult<ContractChangeListRespVO> getContractChangeList(ContractChangeListReqVO reqVO) {
        PageResult<BpmContractChangeDO> bpmContractChangeDOPageResult = bpmContractChangeMapper.selectPage(reqVO);
        PageResult<ContractChangeListRespVO> page = ChangeConverter.INSTANCE.toPage(bpmContractChangeDOPageResult);
        if (ObjectUtil.isNotEmpty(page)) {
            //获取合同id
            List<String> contractIds = page.getList().stream().map(ContractChangeListRespVO::getMainContractId).collect(Collectors.toList());
            List<ContractDO> contractDOS = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(contractIds)) {
                contractDOS = contractMapper.selectList(ContractDO::getId, contractIds);
            }
            Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
            //获取申请人
            List<Long> creatorIds = bpmContractChangeDOPageResult.getList().stream().map(BpmContractChangeDO::getCreator).map(Long::parseLong).collect(Collectors.toList());
            List<AdminUserRespDTO> userList = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(creatorIds)) {
                userList = adminUserApi.getUserList(creatorIds);
            }
            Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(userList, AdminUserRespDTO::getId);
            page.getList().forEach(item -> {
                item.setChangeTypeName(ContractChangeTypeEnums.getInstance(item.getChangeType()).getInfo());
                ContractDO contractDO = contractDOMap.get(item.getMainContractId());
                if (ObjectUtil.isNotEmpty(contractDO)) {
                    item.setContractName(contractDO.getName());
                    item.setContractStatus(contractDO.getStatus());
                    item.setContractStatusName((ContractStatusEnums.getInstance(contractDO.getStatus())).getDesc());
                }
                AdminUserRespDTO user = userMap.get(Long.valueOf(item.getCreator()));
                if (ObjectUtil.isNotEmpty(user)) {
                    item.setCreatorName(user.getNickname());
                }
                item.setResultName(BpmProcessInstanceResultEnum.getInstance(item.getResult()).getDesc());
            });
        }
        return page;
    }

    @Override
    public String applyStatusChange(ContractChangeStatusSaveReqVO vo) throws Exception {
        if (ObjectUtil.isNotEmpty(vo.getId())) {
            BpmContractChangeDO changeDO = ChangeConverter.INSTANCE.changeSaveDO(vo);
            if (ObjectUtil.isNull(changeDO.getFileAddId())){
                changeDO.setFileAddId(0L);
            }
            bpmContractChangeMapper.updateById(changeDO);
            return changeDO.getId();
        }
        //生成变动编号
        generateCode(vo);
        BpmContractChangeDO changeDO = ChangeConverter.INSTANCE.changeSaveDO(vo);
        String uuidString = UUID.randomUUID().toString();
        String id = uuidString.replace("-", "");
        changeDO.setId(id);
        //查询合同状态
        ContractDO contractDO = contractMapper.selectById(vo.getMainContractId());
        changeDO.setProtoStatus(contractDO.getStatus());
        bpmContractChangeMapper.insert(changeDO);
        //存入附件表
        if (ObjectUtil.isNotEmpty(vo.getFiles())) {
            processAttachments(vo.getFiles(), changeDO.getId());
        }
        //发起流程
        IdReqVO reqVO = new IdReqVO().setId(changeDO.getId());
        String processInstanceId = this.submitContractChangeApproveFlowableFast(getLoginUserId(), reqVO);
        //修改合同状态为合同变更
        contractMapper.updateById(new ContractDO().setId(changeDO.getMainContractId()).setStatus(ContractStatusEnums.CONTRACT_CHANGE.getCode()).setIsSign(IfNumEnums.YES.getCode()));
        //提交
        if (ObjectUtil.isNotEmpty(vo.getIsSubmit())) {
            //添加taskId
            if (ObjectUtil.isNotEmpty(processInstanceId)) {
                List<String> processInstanceIds = Arrays.asList(processInstanceId);
                List<BpmTaskAllInfoRespVO> instanceIds = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(getLoginUserId(), processInstanceIds);
                if (ObjectUtil.isNotEmpty(instanceIds)) {
                    for (BpmTaskAllInfoRespVO bpmTaskRespDTO : instanceIds) {
                        BpmTaskApproveReqVO taskApproveReqVO = new BpmTaskApproveReqVO().setTaskId(bpmTaskRespDTO.getTaskId()).setReason(vo.getAdvice());
                        taskService.approveTask(getLoginUserId(), taskApproveReqVO);
                    }

                }
            }
        }
        return id;
    }

    private void generateCode(ContractChangeStatusSaveReqVO vo) {
        if (vo.getChangeType().equals(ContractChangeTypeEnums.CANCELLATION.getCode())) {
            Long count = bpmContractChangeMapper.selectCount(new LambdaQueryWrapperX<BpmContractChangeDO>()
                    .eq(BpmContractChangeDO::getMainContractId, vo.getMainContractId()).eq(BpmContractChangeDO::getChangeType, ContractChangeTypeEnums.CANCELLATION.getCode()));
            int sequenceNumber = (int) (count + 1);
            String formattedSequence = String.format("%02d", sequenceNumber);
            vo.setChangeCode("ZF-" + vo.getContractCode() + "-" + formattedSequence);
        } else if (vo.getChangeType().equals(ContractChangeTypeEnums.CANCEL.getCode())) {
            Long count = bpmContractChangeMapper.selectCount(new LambdaQueryWrapperX<BpmContractChangeDO>()
                    .eq(BpmContractChangeDO::getMainContractId, vo.getMainContractId()).eq(BpmContractChangeDO::getChangeType, ContractChangeTypeEnums.CANCEL.getCode()));
            int sequenceNumber = (int) (count + 1);
            String formattedSequence = String.format("%02d", sequenceNumber);
            vo.setChangeCode("QX-" + vo.getContractCode() + "-" + formattedSequence);
        } else if (vo.getChangeType().equals(ContractChangeTypeEnums.TERMINATED.getCode())) {
            Long count = bpmContractChangeMapper.selectCount(new LambdaQueryWrapperX<BpmContractChangeDO>()
                    .eq(BpmContractChangeDO::getMainContractId, vo.getMainContractId()).eq(BpmContractChangeDO::getChangeType, ContractChangeTypeEnums.TERMINATED.getCode()));
            int sequenceNumber = (int) (count + 1);
            String formattedSequence = String.format("%02d", sequenceNumber);
            vo.setChangeCode("GB-" + vo.getContractCode() + "-" + formattedSequence);
        }
    }

    @Override
    public ContractChangeOneRespVO getContractChangeDetails(IdReqVO vo) throws Exception {
        ContractChangeOneRespVO result = new ContractChangeOneRespVO();
        if (ObjectUtil.isNotEmpty(vo.getContractId())) {
            ContractDO contractDO = contractMapper.selectOne(ContractDO::getId, vo.getContractId());
            result = ContractConverter.INSTANCE.convertDO2Resp(contractDO);
            result.setStatusName(ContractStatusEnums.getInstance(contractDO.getStatus()).getDesc());
            return result;
        }
        String id = vo.getId();
        BpmContractChangeDO changeDO = bpmContractChangeMapper.selectById(id);
        if (ObjectUtil.isNotNull(changeDO)) {
            result = ContractConverter.INSTANCE.changeDO2Resp(changeDO);
        }
        ContractDO contractDO = contractMapper.selectOne(ContractDO::getId, changeDO.getMainContractId());
        result.setStatusName(ContractStatusEnums.getInstance(contractDO.getStatus()).getDesc());
        result.setMainContractName(contractDO.getName());
        result.setMainContractCode(contractDO.getCode());
        AdminUserRespDTO user = adminUserApi.getUser(Long.valueOf(result.getCreator()));
        result.setSubmitterName(user.getNickname());
        if (ObjectUtil.isNotEmpty(result.getFileAddId())) {
            String name = fileApi.getName(result.getFileAddId());
            result.setFileAddName(name);
        }
        List<BusinessFileDO> contractContentFileDOS = businessFileMapper.selectByBusinessId(id);
        if (ObjectUtil.isNotEmpty(contractContentFileDOS)) {
            List<AttachmentVO> files = convertToAttachmentVOList(contractContentFileDOS);
            result.setFiles(files);
        }
        //添加taskId
        if (ObjectUtil.isNotEmpty(changeDO.getProcessInstanceId())) {
            List<String> processInstanceIds = Arrays.asList(changeDO.getProcessInstanceId());
            List<BpmTaskAllInfoRespVO> instanceIds = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(getLoginUserId(), processInstanceIds);
            if (ObjectUtil.isNotEmpty(instanceIds)) {
                for (BpmTaskAllInfoRespVO bpmTaskRespDTO : instanceIds) {
                    result.setTaskId(bpmTaskRespDTO.getTaskId());
                    break;
                }

            }
        }
        //添加部门信息
        result.setDeptId(changeDO.getDeptId());
        DeptRespDTO dept = deptApi.getDept(changeDO.getDeptId());
        if (ObjectUtil.isNotEmpty(dept)) {
            result.setDeptName(dept.getName());
        }
        return result;
    }

    @Override
    public String batchInitiateStatusChangeApproval(Long loginUserId, IdReqVO reqVO) {
        List<String> changeIds = reqVO.getIdList();
        // 一次性查询所有合同变更数据
        List<BpmContractChangeDO> changeDOList = bpmContractChangeMapper.selectList(
                new LambdaQueryWrapperX<BpmContractChangeDO>().in(BpmContractChangeDO::getId, changeIds));
        if (CollectionUtil.isEmpty(changeDOList)) {
            throw exception(EMPTY_DATA_ERROR);
        }
        for (BpmContractChangeDO changeDO : changeDOList) {
            // 校验主合同是否有变动协议还在审批中
            List<BpmContractChangeDO> mainContractChanges = bpmContractChangeMapper.selectList(
                    new LambdaQueryWrapperX<BpmContractChangeDO>()
                            .eq(BpmContractChangeDO::getMainContractId, changeDO.getMainContractId())
            );
            if (CollectionUtil.isNotEmpty(mainContractChanges)) {
                for (BpmContractChangeDO contractChange : mainContractChanges) {
                    BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(contractChange.getResult());
                    if (BpmProcessInstanceResultEnum.PROCESS == resultEnum || BpmProcessInstanceResultEnum.BACK == resultEnum) {
                        throw exception(CONTRACT_CHANGE_APPROVING);
                    }
                }
            }
            // 创建新的变更实例
            BpmContractChangeDO bpmContractChangeDO = new BpmContractChangeDO()
                    .setId(changeDO.getId())
                    .setUserId(changeDO.getUserId())
                    .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
            // 2 发起申请 BPM
            // 2.1 流程变量
            Map<String, Object> processInstanceVariables = new HashMap<>(16);
            processInstanceVariables.put("changeId", changeDO.getId());
            if (Objects.equals(changeDO.getChangeType(), ContractChangeTypeEnums.CHANGE.getCode()) || Objects.equals(changeDO.getChangeType(), ContractChangeTypeEnums.SUPPLEMENT.getCode()) || Objects.equals(changeDO.getChangeType(), ContractChangeTypeEnums.TERMINATE.getCode()) || Objects.equals(changeDO.getChangeType(), ContractChangeTypeEnums.CANCELLATION.getCode())) {
                processInstanceVariables.put("passChangeType", 1);
            } else {
                processInstanceVariables.put("passChangeType", 2);
            }
            // 2.2 创建流程实例
            String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                    new BpmProcessInstanceCreateReqDTO()
                            .setProcessDefinitionKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey())
                            .setVariables(processInstanceVariables)
                            .setBusinessKey(changeDO.getId())
            );
            // 更新流程实例 ID
            bpmContractChangeMapper.updateById(bpmContractChangeDO.setProcessInstanceId(processInstanceId));
        }

        return "true";
    }

    @Override
    public List<ElementRespVO> getElementList() {
        List<ElementDO> elementDOS = elementMapper.selectList();
        return ChangeConverter.INSTANCE.toElementList(elementDOS);
    }

    @Override
    public Long getContractChangeOrgtNum(CommonBpmAutoPageReqVO pageVO) {
        List<String> instanceIdList = new ArrayList<>();
        if (2 == pageVO.getFlag()) {
            //草稿，待审批
            // 查询待办任务
            List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey());
            // 获得 ProcessInstance
            instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                    .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                    .distinct()
                    .collect(Collectors.toList());

        } else if (1 == pageVO.getFlag()) {
            //我发起的，已审批
            Integer taskResult = StatusConstants.TEMP_INTEGER;
            if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
                taskResult = pageVO.getTaskResult();
            }
            //获得已处理任务数据
            List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey(), taskResult);
            //去除已取消的任务。
            processInstanceRelationInfoRespDTOList = EcontractUtil.clearRepealTask(processInstanceRelationInfoRespDTOList);
            //去除待处理列表中的任务
            List<ContractProcessInstanceRelationInfoRespDTO> toDoProcessInstanceRelationList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE.getDefinitionKey());
            List<String> toDoInstanceIdList = toDoProcessInstanceRelationList.stream()
                    .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                    .distinct()
                    .collect(Collectors.toList());
            // 获得 ProcessInstance
            instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                    .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                    .distinct()
                    .collect(Collectors.toList());
            instanceIdList.removeAll(toDoInstanceIdList); // 排除待办任务
        }
        pageVO.setInstanceIdList(instanceIdList);
        Long orgSignetAgentNum = bpmContractChangeMapper.selectContractChangeNum(pageVO);
        return orgSignetAgentNum;
    }

    @Override
    public Boolean checkContractTerminating(String id) {
        Long count = bpmContractChangeMapper.selectCount(new LambdaQueryWrapperX<BpmContractChangeDO>().eq(BpmContractChangeDO::getMainContractId, id)
                .eq(BpmContractChangeDO::getChangeType, ContractChangeTypeEnums.TERMINATE.getCode())
                .eq(BpmContractChangeDO::getResult, 1)

        );
        return 0L != count;
    }

    @Override
    public ChangeRiskListRespVO changeRiskConfirmList(String id) {
        ChangeRiskListRespVO result = new ChangeRiskListRespVO();
        ContractDO contractDO = contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getId, id).select(ContractDO::getId,ContractDO::getPartBName));
        if (ObjectUtil.isNull(contractDO)) {
            throw  exception(DATA_ERROR);
        }
        //在途业务
        List<ApplicationRespVO> applicationRespVOList = new ArrayList<>();
        // 在途变动
        List<BpmContractChangeDO> contractChangeDOList = bpmContractChangeMapper.selectList(new LambdaQueryWrapperX<BpmContractChangeDO>().eq(BpmContractChangeDO::getMainContractId, id)
                .in(BpmContractChangeDO::getResult, BpmProcessInstanceResultEnum.DRAFT.getResult(), BpmProcessInstanceResultEnum.PROCESS.getResult())
        );
        if (CollectionUtil.isNotEmpty(contractChangeDOList)) {
            for (BpmContractChangeDO changeDO : contractChangeDOList) {
                ApplicationRespVO applicationRespVO = new ApplicationRespVO()
                        .setCode(changeDO.getChangeCode())
                        .setName(changeDO.getChangeName())
                        .setId(changeDO.getId())
                        .setRelativeName(contractDO.getPartBName())
                        ;
                ContractChangeTypeEnums typeEnums = ContractChangeTypeEnums.getInstance(changeDO.getChangeType());
                if(ObjectUtil.isNotNull(typeEnums)) {
                    applicationRespVO.setApplyTypeName(typeEnums.getInfo());
                }
                BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(changeDO.getResult());
                if(ObjectUtil.isNotNull(resultEnum)) {
                    applicationRespVO.setStatusName(resultEnum.getDesc());
                }
                applicationRespVOList.add(applicationRespVO);
            }
        }
        // 在途履约
        List<PaymentApplicationDO> applicationDOList = paymentApplicationMapper.selectList(new LambdaQueryWrapperX<PaymentApplicationDO>()
                .eq(PaymentApplicationDO::getContractId,id));
        if(CollectionUtil.isNotEmpty(applicationDOList)) {
            for (PaymentApplicationDO applicationDO : applicationDOList) {
                ApplicationRespVO applicationRespVO = new ApplicationRespVO()
                        .setId(applicationDO.getId())
                        .setName(applicationDO.getTitle())
                        .setCode(applicationDO.getPaymentApplyCode())
                        .setRelativeName(contractDO.getPartBName())
                        ;
                BpmProcessInstanceStatusEnum resultEnum = BpmProcessInstanceStatusEnum.getInstance(applicationDO.getResult());
                if(ObjectUtil.isNotNull(resultEnum)) {
                    applicationRespVO.setStatusName(resultEnum.getDesc());
                    if(BpmProcessInstanceStatusEnum.APPROVE == resultEnum){
                        PaymentApplicationStatusEnums paymentApplicationStatusEnums = PaymentApplicationStatusEnums.getInstance(applicationDO.getStatus());
                        if(ObjectUtil.isNotNull(paymentApplicationStatusEnums)){
                            applicationRespVO.setStatusName(paymentApplicationStatusEnums.getInfo());
                        }
                    }
                }
                CollectionTypeEnums typeEnums = CollectionTypeEnums.getInstance(applicationDO.getCollectionType());
                if(ObjectUtil.isNotNull(typeEnums)) {
                    applicationRespVO.setApplyTypeName(typeEnums.getInfo());
                }
                applicationRespVOList.add(applicationRespVO);
            }
        }
        //待执行计划
        List<ChangeRiskPlanRespVO> riskPlanRespVOList = new ArrayList<>();
        List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                .eq(PaymentScheduleDO::getStatus, PaymentScheduleStatusEnums.TO_DO.getCode())
                .eq(PaymentScheduleDO::getContractId, id).orderByAsc(PaymentScheduleDO::getSort));
        if(CollectionUtil.isNotEmpty(paymentScheduleDOList)) {
            riskPlanRespVOList = PaymentScheduleConverter.INSTANCE.tochangeRespVOList(paymentScheduleDOList);
            List<String> payeeIds = riskPlanRespVOList.stream().map(ChangeRiskPlanRespVO::getPayee).collect(Collectors.toList());
            List<Relative> relatives = relativeMapper.selectList(Relative::getId,payeeIds);
            Map<String,Relative> relativeMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(relatives)) {
                relativeMap = CollectionUtils.convertMap(relatives, Relative::getId);
            }
            for (ChangeRiskPlanRespVO respVO : riskPlanRespVOList) {
                respVO.setMoneyTypeName(MoneyTypeEnums.getInstance(respVO.getMoneyType()).getInfo());
                respVO.setAmountTypeName(AmountTypeEnums.getInstance(respVO.getAmountType()).getInfo());
                if (ObjectUtil.isNotEmpty(respVO.getPayee())) {
                    Relative relative = relativeMap.get(respVO.getPayee());
                    if (ObjectUtil.isNotEmpty(relative)) {
                            respVO.setPayee(relative.getCompanyName());
                    }
                    respVO.setStatusName(PaymentScheduleStatusEnums.getInstance(respVO.getStatus()).getInfo());
                }
            }
        }
        return result.setApplicationRespVOList(applicationRespVOList).setRiskPlanRespVOList(riskPlanRespVOList);
    }
}


