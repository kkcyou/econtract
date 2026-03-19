package com.yaoan.module.econtract.service.bpm.model;

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
import com.yaoan.module.bpm.api.task.dto.SimpleTaskDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.*;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.BatchSubmitReqVO;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.SubmitReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.BpmTaskApproveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.ModelPageRespVO;
import com.yaoan.module.econtract.convert.config.SystemConfigDTOConverter;
import com.yaoan.module.econtract.convert.model.ModelConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.model.ModelBpmDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.mysql.bpm.model.ModelBpmMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.model.SimpleModelMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums;
import com.yaoan.module.econtract.service.contract.TaskService;
import com.yaoan.module.econtract.service.model.ModelService;
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
 * @date: 2023/9/13 19:39
 */
@Slf4j
@Service
public class ModelBpmServiceImpl implements ModelBpmService {
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private SimpleModelMapper simpleModelMapper;
    @Resource
    private ModelBpmMapper modelBpmMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private ModelService modelService;
    @Resource
    private TaskService taskService;

    public String submitOne(Long loginUserId, ModelBpmSubmitCreateReqVO reqVO) throws Exception {

        //审批前前判断模板是否存在
        if (StringUtils.isEmpty(reqVO.getModelId())) {

        }
        SimpleModel model = simpleModelMapper.selectById(reqVO.getModelId());
        if (model == null) {
            throw exception(ErrorCodeConstants.EMPTY_DATA_ERROR);
        }

        //是否有新版本
        PageResult<ModelPageRespVO> modelHistory = modelService.getModelHistory(model.getCode());
        //取出第一个值
        if (modelHistory != null && modelHistory.getList() != null && modelHistory.getList().size() > 0) {
            //判断相关版本是否在审核中
            for (ModelPageRespVO modelPageRespVO : modelHistory.getList()) {
                ModelBpmDO modelBpmDO = modelBpmMapper.selectOne(ModelBpmDO::getModelId, modelPageRespVO.getId());
                //在审核中报异常
                if (modelBpmDO != null && modelBpmDO.getResult() == 1) {
                    throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "已存在新版本，请删除后再更新模板。");
                }
            }
        }

        AdminUserRespDTO userRespDTO = userApi.getUser(loginUserId);
        //1.插入请求单
        ModelBpmDO modelBpmDO = new ModelBpmDO().setReason(reqVO.getApproveIntroduction()).setModelId(reqVO.getModelId()).setModelName(model.getName())
                .setApproveType(ApproveTypeEnum.APPROVING.getCode()).setUserId(loginUserId).setSubmitterName(userRespDTO == null ? "" : userRespDTO.getNickname()).setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());

        modelBpmMapper.insert(modelBpmDO);

        //同步模板库状态，发起审批的模板就属于激活状态，不可以再被更改或者删除了
        SimpleModel updateModel = new SimpleModel();
        updateModel.setId(model.getId());
        updateModel.setApproveStatus(StatusEnums.APPROVING.getCode());
        updateModel.setSubmitTime(LocalDateTime.now());
        simpleModelMapper.updateById(updateModel);
        // 2 发起 BPM
        // 2.1 流程变量
        Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
        processInstanceVariables.put("modelId", modelBpmDO.getModelId());
        String modelBpmId = modelBpmDO.getId();
        // 2.2 流程实例id
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(ActivityConfigurationEnum.MODEL_APPROVE.getDefinitionKey())
                        .setVariables(processInstanceVariables).setBusinessKey(modelBpmId));

        modelBpmMapper.updateById(new ModelBpmDO().setId(modelBpmId).setProcessInstanceId(processInstanceId));

        return modelBpmId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitModelApproveFlowable(Long loginUserId, ModelBpmSubmitCreateReqVO reqVO) throws Exception {
        //判断走审批流
        if (systemConfigApi.ifApprove(ActivityConfigurationEnum.MODEL_APPROVE)) {
            // 走审批流
            return submitOne(loginUserId, reqVO);
        } else {
            // 不走审批流，直接通过
            return fastPassOne(reqVO);
        }
    }

    private String fastPassOne(ModelBpmSubmitCreateReqVO reqVO) {
        SimpleModel simpleModel = new SimpleModel().setId(reqVO.getModelId()).setApproveStatus(BpmProcessInstanceResultEnum.APPROVE.getResult());
        simpleModelMapper.updateById(simpleModel);
        return "success";
    }

    /**
     * 模板审批流页面展示
     */
    @Override
    public PageResult<ModelBpmPageRespVO> getApprovePage(ModelBpmPageReqVO reqVO) {

        // 查询当前用户指定流程定义key的流程实例信息 包含待办和已办 代办包含任务ID，已办信息包含最新一次执行结果。
        // Collections.singleton(PROCESS_KEY):返回一个不可变集合，该集合仅包含 PROCESS_KEY 作为其唯一元素。这个集合不能被修改，且大小固定为 1。
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(SecurityFrameworkUtils.getLoginUserId(), Collections.singleton(ActivityConfigurationEnum.MODEL_APPROVE.getDefinitionKey()));

        if (CollectionUtil.isEmpty(allRelationProcessInstanceInfos)) {
            return new PageResult<>();
        }
        // 将所有info元素的流程实例id 筛选出来重组list
        List<String> instanceList = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 将所有当前用户相关的流程实例存入reqVO
        reqVO.setProcessInstanceIds(instanceList);
        //筛出相关流程DO，重组分页VO
        LambdaQueryWrapperX<ModelBpmDO> wrapperX = enhanceWrapper(reqVO);
        PageResult<ModelBpmDO> modelBpmDOPageResult = modelBpmMapper.selectPage(reqVO, wrapperX);
        //空值校验
        if (CollectionUtil.isEmpty(modelBpmDOPageResult.getList())) {
            return new PageResult<ModelBpmPageRespVO>()
                    .setList(Collections.emptyList())
                    .setTotal(modelBpmDOPageResult.getTotal())
                    ;
        }
        //转换respVO
        PageResult<ModelBpmAllVO> result = ModelConverter.INSTANCE.convert2AllVO(modelBpmDOPageResult);

        //增强赋值字段
        PageResult<ModelBpmAllVO> allVoPageResult = enhance(result, allRelationProcessInstanceInfos);
        PageResult<ModelBpmPageRespVO> respVOPageResult = convert2BpmResVO(allVoPageResult);

        return respVOPageResult;
    }

    private PageResult<ModelBpmPageRespVO> convert2BpmResVO(PageResult<ModelBpmAllVO> allVoPageResult) {
        Long loginUserId = null;
        if (CollectionUtil.isNotEmpty(allVoPageResult.getList())) {
            loginUserId = allVoPageResult.getList().get(0).getUserId();
        }
        List<SimpleModel> models = simpleModelMapper.selectList(new LambdaQueryWrapperX<SimpleModel>().eq(SimpleModel::getCreator, String.valueOf(loginUserId)));
        Map<String, SimpleModel> modelMap = CollectionUtils.convertMap(models, SimpleModel::getId);
        PageResult<ModelBpmPageRespVO> modelResPage = new PageResult<ModelBpmPageRespVO>();
        modelResPage.setTotal(allVoPageResult.getTotal());


        //流程信息
        List<String> doIdList = models.stream().map(SimpleModel::getId).collect(Collectors.toList());
        List<ModelBpmDO> bpmDOList = modelBpmMapper.selectList(new LambdaQueryWrapperX<ModelBpmDO>().inIfPresent(ModelBpmDO::getModelId, doIdList));
        Map<String, ModelBpmDO> bpmDOMap = CollectionUtils.convertMap(bpmDOList, ModelBpmDO::getModelId);
        Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        Map<String, BpmTaskAllInfoRespVO> taskEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        List<ModelBpmPageRespVO> respVOList = new ArrayList<ModelBpmPageRespVO>();
        List<String> instanceList = bpmDOList.stream().map(ModelBpmDO::getProcessInstanceId).collect(Collectors.toList());

        List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
        List<BpmTaskAllInfoRespVO> taskEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
        if (CollectionUtil.isNotEmpty(instanceList)) {
            taskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);
            taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
        }
        //有结束时间的流程任务
        if (CollectionUtil.isNotEmpty(instanceList)) {
            taskEndTimeAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);
            taskEndTimeAllInfoRespVOList = EcontractUtil.distinctTaskHaveEndTime(taskEndTimeAllInfoRespVOList);
            taskEndTimeMap = CollectionUtils.convertMap(taskEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
        }
        for (ModelBpmAllVO allVO : allVoPageResult.getList()) {

            ModelBpmPageRespVO respVO = new ModelBpmPageRespVO();

            ModelBpmDO bpmDO = bpmDOMap.get(allVO.getModelId());
            if (ObjectUtil.isNotNull(bpmDO)) {

                //最新审批时间
                //获取任务endTime，便知道审批时间
                BpmTaskAllInfoRespVO endTimeTask = taskEndTimeMap.get(bpmDO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(endTimeTask)) {
                    respVO.setApproveTime(endTimeTask.getEndTime());
                }
            }

            respVO.setModelId(allVO.getModelId());
            respVO.setApproveContent(allVO.getModelName());
            respVO.setSubmitter(allVO.getUserName());
            respVO.setApproveStatus(allVO.getApproveType());
            respVO.setProcessInstanceId(allVO.getProcessInstanceId());
            respVO.setTaskId(allVO.getTaskId());
            respVO.setResult(allVO.getResult());
            respVO.setCode(allVO.getCode());

            SimpleModel model = modelMap.get(allVO.getModelId());
            if (model != null) {
                respVO.setCode(model.getCode());
                respVO.setName(model.getName());
                respVO.setSubmitTime(model.getSubmitTime());
            }

            respVOList.add(respVO);
        }
        modelResPage.setList(respVOList);
        return modelResPage;
    }

    /**
     * 增强赋值字段给respVO
     */
    private PageResult<ModelBpmAllVO> enhance(PageResult<ModelBpmAllVO> result, List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos) {

        //将审批结果为 处理中 的流程实例找出来
        List<ContractProcessInstanceRelationInfoRespDTO> processingInstanceList = allRelationProcessInstanceInfos.stream()
                .filter(item -> item.getProcessResult()
                        .equals(BpmProcessInstanceResultEnum.PROCESS.getResult())).collect(Collectors.toList());
        // processInstanceId字段作为键，taskId字段作为对应的值。存储在processTaskMap中。
        Map<String, String> processTaskMap = CollectionUtils.convertMap(processingInstanceList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId, ContractProcessInstanceRelationInfoRespDTO::getTaskId);
        // 将用户找出来
        List<Long> userIds = result.getList().stream().map(ModelBpmAllVO::getUserId).collect(Collectors.toList());
        List<AdminUserRespDTO> userInfoList = userApi.getUserList(userIds);
        Map<Long, AdminUserRespDTO> userInfoMap = CollectionUtils.convertMap(userInfoList, AdminUserRespDTO::getId);
        // 遍历用户
        result.getList().forEach(item -> {
            AdminUserRespDTO userInfo = userInfoMap.get(item.getUserId());
            //赋值用户名
            if (userInfo != null) {
                item.setUserName(userInfo.getNickname());
            }
            // 赋值流程状态（审核状态）
            BpmProcessInstanceResultEnum instance = BpmProcessInstanceResultEnum.getInstance(item.getResult());
            if (instance != null) {
                item.setApproveType(instance.getDesc());
            }
            // 赋值流程实例对应的待办任务
            String taskIdStr = processTaskMap.get(item.getProcessInstanceId());
            if (StringUtils.isNotBlank(taskIdStr)) {
                item.setTaskId(taskIdStr);
            }
        });

        return result;
    }

    /**
     * 增强条件查询
     */
    private LambdaQueryWrapperX<ModelBpmDO> enhanceWrapper(ModelBpmPageReqVO reqVO) {
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

        LambdaQueryWrapperX<ModelBpmDO> wrapperX = new LambdaQueryWrapperX<ModelBpmDO>()
                .orderByDesc(ModelBpmDO::getCreateTime)
                .eqIfPresent(ModelBpmDO::getResult, reqVO.getResult())
                .betweenIfPresent(ModelBpmDO::getCreateTime, reqVO.getStartCreateTime(), reqVO.getEndCreateTime())
                .betweenIfPresent(ModelBpmDO::getUpdateTime, reqVO.getStartApproveSuccessTime(), reqVO.getEndApproveSuccessTime())
                //将 流程实例存在的流程DO找出
                .inIfPresent(ModelBpmDO::getProcessInstanceId, reqVO.getProcessInstanceIds());
        //模糊查询的并集
        if (CollectionUtil.isNotEmpty(userIdsAsString)) {
            List<String> finalUserIdsAsString = userIdsAsString;
            wrapperX.and(w -> w
                    .or()
                    .like(ModelBpmDO::getModelName, reqVO.getSearchText())
                    .or()
                    .in(ModelBpmDO::getCreator, finalUserIdsAsString)
            );
        } else {
            //如果userIdsAsString为空，说明没有匹配的用户，单查模板名称就可以了
            wrapperX.likeIfPresent(ModelBpmDO::getModelName, searchText);
        }
        return wrapperX;
    }

    @Override
    public ModelBigListApproveRespVO getBpmAllTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        // 查询所有任务(已过滤掉已取消的任务)
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.MODEL_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<Model> doPageResult = modelMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public ModelBigListApproveRespVO getBpmDoneTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        // 查询待办任务
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        //获得已处理任务数据(已过滤掉已取消的任务)
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.MODEL_APPROVE.getDefinitionKey(), taskResult);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<Model> doPageResult = modelMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }


    @Override
    public ModelBigListApproveRespVO getBpmToDoTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        // 查询待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.MODEL_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<Model> doPageResult = modelMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    private ModelBigListApproveRespVO enhanceBpmPage(PageResult<Model> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {
        List<Model> doList = doPageResult.getList();
        List<ModelListApproveRespVO> respVOList = ModelConverter.INSTANCE.convertBpmDO2Resp(doList);
        if (CollectionUtil.isNotEmpty(doList)) {
            //任务时间
            List<String> taskIdList = new ArrayList<>(instanceRelationInfoRespDTOMap.keySet());
            List<SimpleTaskDTO> bpmTaskList = new ArrayList<SimpleTaskDTO>();
            Map<String,SimpleTaskDTO> bpmTaskMap = new HashMap<String,SimpleTaskDTO>();
            if (CollectionUtil.isNotEmpty(taskIdList)) {
                bpmTaskList = bpmTaskApi.getBpmTaskByTaskIds(taskIdList);
                if(CollectionUtil.isNotEmpty(bpmTaskList)) {
                    bpmTaskMap=CollectionUtils.convertMap(bpmTaskList,SimpleTaskDTO::getTaskId);
                }
            }

            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            //合同类型map
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            List<String> modelIdList = doList.stream().map(Model::getId).collect(Collectors.toList());
            List<ModelBpmDO> modelBpmDOList = modelBpmMapper.selectList(new LambdaQueryWrapperX<ModelBpmDO>().inIfPresent(ModelBpmDO::getModelId, modelIdList));
            Map<String, ModelBpmDO> modelBpmDOMap = CollectionUtils.convertMap(modelBpmDOList, ModelBpmDO::getModelId);
            //发起人信息map
            List<AdminUserRespDTO> userRespDTOList = userApi.getUserList();
            Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);

            //流程信息
            List<String> doIdList = doList.stream().map(Model::getId).collect(Collectors.toList());
            List<ModelBpmDO> bpmDOList = modelBpmMapper.selectList(new LambdaQueryWrapperX<ModelBpmDO>().inIfPresent(ModelBpmDO::getModelId, doIdList));
            Map<String, ModelBpmDO> bpmDOMap = CollectionUtils.convertMap(bpmDOList, ModelBpmDO::getModelId);
            List<String> instanceList = bpmDOList.stream().map(ModelBpmDO::getProcessInstanceId).collect(Collectors.toList());

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

            for (ModelListApproveRespVO respVO : respVOList) {
                ContractType contractType = contractTypeMap.get(respVO.getContractType());
                if (ObjectUtil.isNotNull(contractType)) {
                    respVO.setContractTypeName(contractType.getName());
                }
                ModelBpmDO modelBpmDO = modelBpmDOMap.get(respVO.getId());
                if (ObjectUtil.isNotNull(modelBpmDO)) {
                    //已审批任务的状态赋值
                    ContractProcessInstanceRelationInfoRespDTO infoRespDTO = instanceRelationInfoRespDTOMap.get(modelBpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(infoRespDTO)) {
                        respVO.setDoneTaskResult(infoRespDTO.getProcessResult());

                    }
                    //最新审批时间
                    respVO.setApproveTime(modelBpmDO.getUpdateTime());

                    //流程实例
                    respVO.setProcessInstanceId(modelBpmDO.getProcessInstanceId());
                    //提交人
                    AdminUserRespDTO userRespDTO = userRespDTOMap.get(Long.valueOf(modelBpmDO.getCreator()));
                    if (ObjectUtil.isNotNull(userRespDTO)) {
                        respVO.setSubmitter(userRespDTO.getNickname());
                    }
                    //流程状态
                    respVO.setResult(modelBpmDO.getResult());
                    //流程任务
                    ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO = instanceRelationInfoRespDTOMap.get(modelBpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)) {
                        respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
                        //任务时间
                        SimpleTaskDTO simpleTaskDTO=bpmTaskMap.get(processInstanceRelationInfoRespDTO.getTaskId());
                        if(ObjectUtil.isNotNull(simpleTaskDTO)) {
                            respVO.setApproveTime(simpleTaskDTO.getEndTime());
                        }
                    }
                    //历史任务信息
                    BpmTaskAllInfoRespVO historyTask = taskEndTimeMap.get(modelBpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(historyTask)) {
                        respVO.setHisTaskResult(historyTask.getResult());
                    }
                    //全部审批列表的审批状态(找不到当前登录人的待办任务，既是已办任务)
                    BpmTaskAllInfoRespVO toDoTask = toDoTaskMap.get(modelBpmDO.getProcessInstanceId());
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
            PageResult<ModelListApproveRespVO> pageResult = new PageResult<ModelListApproveRespVO>();
            pageResult.setList(respVOList).setTotal(doPageResult.getTotal());
            ModelBigListApproveRespVO respVO = new ModelBigListApproveRespVO().setPageResult(pageResult);
            //获取配置
            respVO.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.MODEL_APPROVE)));
            return respVO;
        }
        ModelBigListApproveRespVO result = new ModelBigListApproveRespVO()
                .setPageResult(new PageResult<ModelListApproveRespVO>()
                        .setList(Collections.emptyList())
                        .setTotal(doPageResult.getTotal())
                );
        result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.MODEL_APPROVE)));
        return result;
    }


    /**
     * 模板批量请求审批发起
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitModelApproveFlowableBatch(Long loginUserId, BatchSubmitReqVO vo) {
        //判断走审批流
        if (systemConfigApi.ifApprove(ActivityConfigurationEnum.MODEL_APPROVE)) {
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
        List<String> modelIdList = list.stream().map(SubmitReqVO::getBusinessId).collect(Collectors.toList());
        List<SimpleModel> modelList = new ArrayList<SimpleModel>();
        for (String modelId : modelIdList) {
            SimpleModel model = new SimpleModel();
            model.setId(modelId)
                    .setApproveStatus(BpmProcessInstanceResultEnum.APPROVE.getResult())
                    .setApproveTime(LocalDateTime.now());
            modelList.add(model);
        }
        simpleModelMapper.updateBatch(modelList);

    }

    public void submitModelBatch(Long loginUserId, BatchSubmitReqVO vo) {
        //分组：首次提交（草稿 或者 已取消的）
        List<SubmitReqVO> submitVOList = FlowableUtil.getSubmitReqVO(vo);
        //分组：被退回的
        List<SubmitReqVO> backVOList = FlowableUtil.getBackReqVO(vo);
        //批量提交
//        List<ModelBpmSubmitCreateReqVO> submitTermAddVOList = enhanceBusinessSubmitReq(submitVOList);
        List<ModelBpmSubmitCreateReqVO> submitTermAddVOList = FlowableUtil.enhanceBusinessSubmitReq(ModelBpmSubmitCreateReqVO.class, submitVOList);

        List<String> backTaskIdList = FlowableUtil.enhanceBackTaskIdList(backVOList);
        if (CollectionUtil.isNotEmpty(submitTermAddVOList)) {
            for (ModelBpmSubmitCreateReqVO item : submitTermAddVOList) {
                try {
                    submitModelApproveFlowable(loginUserId, item);
                } catch (Exception e) {
                    log.error("submitModelBatch 方法发生异常。");
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


}
