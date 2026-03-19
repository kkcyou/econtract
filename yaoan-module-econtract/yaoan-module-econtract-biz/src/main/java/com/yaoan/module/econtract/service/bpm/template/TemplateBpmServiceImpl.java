package com.yaoan.module.econtract.service.bpm.template;

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
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.template.vo.*;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.BatchSubmitReqVO;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.SubmitReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.BpmTaskApproveReqVO;
import com.yaoan.module.econtract.convert.config.SystemConfigDTOConverter;
import com.yaoan.module.econtract.convert.contracttemplate.ContractTemplateConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.template.TemplateBpmDO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.bpm.template.TemplateBpmMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums;
import com.yaoan.module.econtract.service.contract.TaskService;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.flowable.FlowableUtil;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.FLOWABLE_SUBMIT_REQUEST_ERROR;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/14 14:50
 */
@Slf4j
@Service
public class TemplateBpmServiceImpl implements TemplateBpmService {
    /**
     * 流程定义KEY
     */
    public static final String PROCESS_KEY = "template_submit_approve";

    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private ContractTemplateMapper templateMapper;
    @Resource
    private TemplateBpmMapper templateBpmMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private TaskService taskService;

    /**
     * 范本-提交审批
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitTemplateApproveFlowable(Long loginUserId, TemplateBpmSubmitCreateReqVO reqVO) {
        ContractTemplate template = templateMapper.selectById(reqVO.getId());
        if (template == null) {
            throw exception(ErrorCodeConstants.EMPTY_DATA_ERROR);
        }

        AdminUserRespDTO userRespDTO = userApi.getUser(loginUserId);
        //1.插入请求单
        TemplateBpmDO templateBpmDO = new TemplateBpmDO().setReason(reqVO.getApproveIntroduction()).setTemplateId(template.getId()).setTemplateName(template.getName())
                .setApproveType(ApproveTypeEnum.APPROVING.getCode()).setUserId(loginUserId).setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        templateBpmMapper.insert(templateBpmDO);
        //同步范本库状态
        template.setApproveStatus(StatusEnums.APPROVING.getCode());
        template.setSubmitTime(LocalDateTime.now());
        templateMapper.updateById(template);

        // 2 发起 BPM
        // 2.1 流程变量
        Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
        processInstanceVariables.put("templateId", templateBpmDO.getTemplateId());
        String templateBpmId = templateBpmDO.getId();
        // 2.2 流程实例id
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(templateBpmId));

        templateBpmMapper.updateById(new TemplateBpmDO().setId(templateBpmId).setProcessInstanceId(processInstanceId));

        return templateBpmId;
    }

    /**
     * 范本审批-列表展示
     */
    @Override
    public PageResult<TemplateBpmPageRespVO> getTemplateApprovePage(TemplateBpmPageReqVO reqVO) {
        // 查询当前用户指定流程定义key的流程实例信息 包含待办和已办 代办包含任务ID，已办信息包含最新一次执行结果。
        // Collections.singleton(PROCESS_KEY):返回一个不可变集合，该集合仅包含 PROCESS_KEY 作为其唯一元素。这个集合不能被修改，且大小固定为 1。.
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(getLoginUserId(), Collections.singleton(PROCESS_KEY));

        if (CollectionUtil.isEmpty(allRelationProcessInstanceInfos)) {
            return new PageResult<>();
        }
        // 将所有info元素的流程实例id 筛选出来重组list
        List<String> instanceList = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 将所有当前用户相关的流程实例存入reqVO
        reqVO.setProcessInstanceIds(instanceList);
        //筛出相关流程DO，重组分页VO
        LambdaQueryWrapperX<TemplateBpmDO> wrapperX = enhanceWrapper(reqVO);
        PageResult<TemplateBpmDO> bpmDOPageResult = templateBpmMapper.selectPage(reqVO, wrapperX);
        //空值校验
        //空值校验
        if (CollectionUtil.isEmpty(bpmDOPageResult.getList())) {
            return new PageResult<TemplateBpmPageRespVO>()
                    .setList(Collections.emptyList())
                    .setTotal(bpmDOPageResult.getTotal())
                    ;
        }

        return enhance(bpmDOPageResult, allRelationProcessInstanceInfos);
    }

    private PageResult<TemplateBpmPageRespVO> enhance(PageResult<TemplateBpmDO> result, List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos) {

        //将审批结果为 处理中 的流程实例找出来
        List<ContractProcessInstanceRelationInfoRespDTO> processingInstanceList = allRelationProcessInstanceInfos.stream()
                .filter(item -> item.getProcessResult()
                        .equals(BpmProcessInstanceResultEnum.PROCESS.getResult())).collect(Collectors.toList());
        // processInstanceId字段作为键，taskId字段作为对应的值。存储在processTaskMap中。
        Map<String, String> processTaskMap = CollectionUtils.convertMap(processingInstanceList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId, ContractProcessInstanceRelationInfoRespDTO::getTaskId);
        // 将用户找出来
        List<Long> userIds = result.getList().stream().map(TemplateBpmDO::getUserId).collect(Collectors.toList());
        List<AdminUserRespDTO> userInfoList = userApi.getUserList(userIds);
        Map<Long, AdminUserRespDTO> userInfoMap = CollectionUtils.convertMap(userInfoList, AdminUserRespDTO::getId);

        List<TemplateBpmPageRespVO> templateRespVoList = new ArrayList<TemplateBpmPageRespVO>();

        Long loginUserId = null;
        if (CollectionUtil.isNotEmpty(result.getList())) {
            loginUserId = result.getList().get(0).getUserId();
        }
        List<ContractTemplate> templates = templateMapper.selectList(new LambdaQueryWrapperX<ContractTemplate>().eq(ContractTemplate::getCreator, String.valueOf(loginUserId)));
        Map<String, ContractTemplate> templateMap = CollectionUtils.convertMap(templates, ContractTemplate::getId);
        // 遍历用户
        for (TemplateBpmDO item : result.getList()) {
            TemplateBpmPageRespVO respVO = new TemplateBpmPageRespVO();
            AdminUserRespDTO userInfo = userInfoMap.get(item.getUserId());
            //赋值用户名
            if (userInfo != null) {
                respVO.setSubmitter(userInfo.getNickname());
            }
            // 赋值流程状态（审核状态）
            BpmProcessInstanceResultEnum instance = BpmProcessInstanceResultEnum.getInstance(item.getResult());
            if (instance != null) {
                respVO.setApproveStatus(instance.getDesc());
            }
            // 赋值流程实例对应的待办任务
            String taskIdStr = processTaskMap.get(item.getProcessInstanceId());
            if (StringUtils.isNotBlank(taskIdStr)) {
                respVO.setTaskId(taskIdStr);
            }
            respVO.setTemplateId(item.getTemplateId());
            respVO.setApproveContent(item.getTemplateName());
            respVO.setSubmitTime(item.getCreateTime());
            if (!item.getUpdateTime().equals(item.getCreateTime())) {
                respVO.setApproveTime(item.getUpdateTime());
            }
            respVO.setProcessInstanceId(item.getProcessInstanceId());
            respVO.setTaskId(taskIdStr);
            respVO.setResult(item.getResult());
            ContractTemplate template = templateMap.get(item.getTemplateId());
            if (template != null) {
                respVO.setCode(template.getCode());
                respVO.setName(template.getName());
            }
            templateRespVoList.add(respVO);
        }

        PageResult<TemplateBpmPageRespVO> respVOPageResult = new PageResult<TemplateBpmPageRespVO>();
        respVOPageResult.setList(templateRespVoList);
        respVOPageResult.setTotal(result.getTotal());

        return respVOPageResult;
    }

    /**
     * 增强条件查询
     */
    private LambdaQueryWrapperX<TemplateBpmDO> enhanceWrapper(TemplateBpmPageReqVO reqVO) {
        String searchText = reqVO.getSearchText();
        List<AdminUserRespDTO> nickUsers = new ArrayList<AdminUserRespDTO>();
        List<Long> nickUserIds = new ArrayList<Long>();
        List<String> userIdsAsString = new ArrayList<String>();
        if (StringUtils.isNotBlank(searchText)) {
            nickUsers = userApi.getUserListLikeNickname(searchText);
            if (CollectionUtil.isNotEmpty(nickUsers)) {
                //将昵称模糊查询选中的userId组成List
                nickUserIds = nickUsers.stream()
                        .map(AdminUserRespDTO::getId)
                        .collect(Collectors.toList());
            }
            //将Long转为String
            userIdsAsString = nickUserIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        LambdaQueryWrapperX<TemplateBpmDO> wrapperX = new LambdaQueryWrapperX<TemplateBpmDO>()
                .orderByDesc(TemplateBpmDO::getCreateTime)
                .eqIfPresent(TemplateBpmDO::getResult, reqVO.getResult())
                .betweenIfPresent(TemplateBpmDO::getCreateTime, reqVO.getStartCreateTime(), reqVO.getEndCreateTime())
                .betweenIfPresent(TemplateBpmDO::getUpdateTime, reqVO.getStartApproveSuccessTime(), reqVO.getEndApproveSuccessTime())
                //将 流程实例存在的流程DO找出
                .inIfPresent(TemplateBpmDO::getProcessInstanceId, reqVO.getProcessInstanceIds());
        //模糊查询的并集
        if (CollectionUtil.isNotEmpty(userIdsAsString)) {
            List<String> finalUserIdsAsString = userIdsAsString;
            wrapperX.and(w -> w
                    .or()
                    .like(TemplateBpmDO::getTemplateName, reqVO.getSearchText())
                    .or()
                    .in(TemplateBpmDO::getCreator, finalUserIdsAsString)
            );
        } else {
            //如果userIdsAsString为空，说明没有匹配的用户，单查模板名称就可以了
            wrapperX.likeIfPresent(TemplateBpmDO::getTemplateName, searchText);
        }
        return wrapperX;
    }

    @Override
    public BigTemplateListApproveRespVO getBpmAllTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        // 查询所有任务(已过滤掉已取消的任务)
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.TEMPLATE_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractTemplate> doPageResult = templateMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);

    }

    @Override
    public BigTemplateListApproveRespVO getBpmDoneTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        // 查询待办任务
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        //获得已处理任务数据(已过滤掉已取消的任务)
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.TEMPLATE_APPROVE.getDefinitionKey(), taskResult);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractTemplate> doPageResult = templateMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);

    }

    @Override
    public BigTemplateListApproveRespVO getBpmToDoTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        // 查询待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.TEMPLATE_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractTemplate> doPageResult = templateMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);

    }

    private BigTemplateListApproveRespVO enhanceBpmPage(PageResult<ContractTemplate> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {
        List<ContractTemplate> doList = doPageResult.getList();
        List<TemplateListApproveRespVO> respVOList = ContractTemplateConverter.INSTANCE.convertBpmDO2Resp(doList);
        if (CollectionUtil.isNotEmpty(doList)) {
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            //合同类型map
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            //发起人信息map
            List<AdminUserRespDTO> userRespDTOList = userApi.getUserList();
            Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);

            //流程信息
            List<String> doIdList = doList.stream().map(ContractTemplate::getId).collect(Collectors.toList());
            List<TemplateBpmDO> bpmDOList = templateBpmMapper.selectList(new LambdaQueryWrapperX<TemplateBpmDO>().inIfPresent(TemplateBpmDO::getTemplateId, doIdList));
            Map<String, TemplateBpmDO> bpmDOMap = CollectionUtils.convertMap(bpmDOList, TemplateBpmDO::getTemplateId);
            List<String> instanceList = bpmDOList.stream().map(TemplateBpmDO::getProcessInstanceId).collect(Collectors.toList());

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

            for (TemplateListApproveRespVO respVO : respVOList) {
                ContractType contractType = contractTypeMap.get(respVO.getContractType());
                if (ObjectUtil.isNotNull(contractType)) {
                    respVO.setContractTypeName(contractType.getName());
                }
                TemplateBpmDO bpmDO = bpmDOMap.get(respVO.getId());
                if (ObjectUtil.isNotNull(bpmDO)) {
                    //提交时间
                    respVO.setSubmitTime(bpmDO.getCreateTime());
                    //最新审批时间
                    //获取任务endTime，便知道审批时间
                    BpmTaskAllInfoRespVO endTimeTask = taskEndTimeMap.get(bpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(endTimeTask)) {
                        respVO.setApproveTime(endTimeTask.getEndTime());
                    }
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
                    //已审批任务的状态赋值
                    ContractProcessInstanceRelationInfoRespDTO infoRespDTO = instanceRelationInfoRespDTOMap.get(bpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(infoRespDTO)) {
                        respVO.setDoneTaskResult(infoRespDTO.getProcessResult());
                    }
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
                }
            }
            PageResult<TemplateListApproveRespVO> pageResult = new PageResult<TemplateListApproveRespVO>();
            pageResult.setList(respVOList).setTotal(doPageResult.getTotal());
            BigTemplateListApproveRespVO respVO = new BigTemplateListApproveRespVO().setPageResult(pageResult);
            //获取配置
            respVO.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.TEMPLATE_APPROVE)));
            return respVO;
        }
        BigTemplateListApproveRespVO result = new BigTemplateListApproveRespVO()
                .setPageResult(new PageResult<TemplateListApproveRespVO>()
                        .setList(Collections.emptyList())
                        .setTotal(doPageResult.getTotal())
                );
        result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.TEMPLATE_APPROVE)));
        return result;
    }

    /**
     * 批量提交
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitApproveFlowableBatch(Long loginUserId, BatchSubmitReqVO vo) {
        //判断走审批流
        if (systemConfigApi.ifApprove(ActivityConfigurationEnum.TEMPLATE_APPROVE)) {
            // 走审批流
            submitModelBatch(loginUserId, vo);
        } else {
            // 不走审批流，直接通过
            fastPassModel(vo);
        }
        return "success";
    }

    /**
     * 批量通过模板（不走审批流）
     */
    private void fastPassModel(BatchSubmitReqVO vo) {
        List<SubmitReqVO> list = vo.getSubmitReqList();
        if (CollectionUtil.isEmpty(list)) {
            throw exception(FLOWABLE_SUBMIT_REQUEST_ERROR);
        }
        List<String> entityIdList = list.stream().map(SubmitReqVO::getBusinessId).collect(Collectors.toList());
        List<ContractTemplate> entityList = new ArrayList<ContractTemplate>();
        for (String entityId : entityIdList) {
            ContractTemplate entity = new ContractTemplate();
            entity.setId(entityId)
                    .setApproveStatus(BpmProcessInstanceResultEnum.APPROVE.getResult())
                    .setApproveTime(LocalDateTime.now());
            entityList.add(entity);
        }
        templateMapper.updateBatch(entityList);

    }

    public void submitModelBatch(Long loginUserId, BatchSubmitReqVO vo) {
        //分组：首次提交
        List<SubmitReqVO> submitVOList = vo.getSubmitReqList().stream()
                .filter(item -> item.getProcessInstanceId() == null || item.getProcessInstanceId().isEmpty())
                .collect(Collectors.toList());
        //分组：被退回的
        List<SubmitReqVO> backVOList = vo.getSubmitReqList().stream()
                .filter(item -> item.getProcessInstanceId() != null || StringUtils.isNotBlank(item.getProcessInstanceId()))
                .collect(Collectors.toList());
        //批量提交
        List<TemplateBpmSubmitCreateReqVO> submitEntityAddVOList = FlowableUtil.enhanceBusinessSubmitReq(TemplateBpmSubmitCreateReqVO.class, submitVOList);

        List<String> backTaskIdList = enhanceBackTaskIdList(backVOList);
        if (CollectionUtil.isNotEmpty(submitEntityAddVOList)) {
            for (TemplateBpmSubmitCreateReqVO item : submitEntityAddVOList) {
                try {
                    submitTemplateApproveFlowable(loginUserId, item);
                } catch (Exception e) {
                    log.error("提交申请 方法发生异常。");
                    e.printStackTrace();
                    throw exception(FLOWABLE_SUBMIT_REQUEST_ERROR);

                }
            }
        }
        //批量通过退回任务
        if (CollectionUtil.isNotEmpty(backTaskIdList)) {
            Long userId = SecurityFrameworkUtils.getLoginUserId();
            for (String taskId : backTaskIdList) {
                BpmTaskApproveReqVO taskApproveReqVO = new BpmTaskApproveReqVO().setTaskId(taskId);
                try {
                    taskService.approveTask(userId, taskApproveReqVO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<String> enhanceBackTaskIdList(List<SubmitReqVO> backVOList) {
        return backVOList.stream().map(SubmitReqVO::getTaskId)
                .collect(Collectors.toList());
    }
}
