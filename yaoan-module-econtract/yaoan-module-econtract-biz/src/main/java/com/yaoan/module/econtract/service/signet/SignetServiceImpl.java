package com.yaoan.module.econtract.service.signet;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.*;
import com.yaoan.module.bpm.enums.model.FlowableModelEnums;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.contract.vo.BpmTaskApproveReqVO;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.AttachmentVO;
import com.yaoan.module.econtract.controller.admin.signet.vo.*;
import com.yaoan.module.econtract.convert.signet.SignetConverter;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.signet.BpmContractSignetDO;
import com.yaoan.module.econtract.dal.dataobject.signet.ContractSignetDO;
import com.yaoan.module.econtract.dal.dataobject.signet.ContractSignetSpecsDO;
import com.yaoan.module.econtract.dal.mysql.businessfile.BusinessFileMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.signet.BpmContractSignetMapper;
import com.yaoan.module.econtract.dal.mysql.signet.ContractSignetMapper;
import com.yaoan.module.econtract.dal.mysql.signet.ContractSignetSpecsMapper;
import com.yaoan.module.econtract.dal.mysql.signet.ContractSignetTypeMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.signet.SignetStatusEnums;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.econtract.service.contract.TaskService;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.dept.DeptApi;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.EcontractOrgApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.api.user.dto.EcontractOrgDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis.utils.Admin;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.econtract.enums.StatusConstants.TEMP_INTEGER;


@Slf4j
@Service
public class SignetServiceImpl implements SignetService {
    public static final String PROCESS_KEY_BOTH_OLD = "ecms_contract_confirm_sign_both";
    public static final String PROCESS_KEY_BOTH = ActivityConfigurationEnum.ECMS_CONTRACT_BOTH.getDefinitionKey();
    //主合同签署-三方
    public static final String PROCESS_KEY_TRIPARTITE_NOT = "ecms_contract_confirm_sign_tripartite";
    public static final String PROCESS_KEY_TRIPARTITE = ActivityConfigurationEnum.ECMS_CONTRACT_TRIPARTITE.getDefinitionKey();
    //签署阶段标识
    public static final String SIGN = "%签署%";
    @Resource
    private ContractSignetMapper contractSignetMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    private ContractSignetTypeMapper contractSignetTypeMapper;
    @Resource
    private ContractSignetSpecsMapper contractSignetSpecsMapper;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private BpmContractSignetMapper bpmContractSignetMapper;
    @Resource
    private BusinessFileMapper businessFileMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private TaskService taskService;
    @Resource
    private ContractService contractService;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private RelativeMapper relativeMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String increaseSignet(SignetCreateReqVO vo) {
        ContractSignetDO voToEntity = SignetConverter.INSTANCE.createVoToEntity(vo);
        //获取用户公司id
        Long companyId = getCompanyId();
        voToEntity.setCompanyId(companyId);
        verifyCodeAndName(vo);
        DataPermissionUtils.executeIgnore(() -> {
            AdminUserRespDTO user = adminUserApi.getUser(vo.getSealAdminId());
            if (ObjectUtil.isNotEmpty(user)) {
                voToEntity.setSealAdminName(user.getNickname());
            }
        });
        if (ObjectUtil.isNotEmpty(vo.getId())) {
           //判断印章有效开始时间是否大于当前时间，修改印章状态为未启用
            if(vo.getIsPermanent().equals(1)){
                voToEntity.setSealStatus(SignetStatusEnums.ENABLED.getCode());
            }else{
                LocalDate localDate = LocalDate.now();
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                if (voToEntity.getSealStartDate().before(date)) {
                    voToEntity.setSealStatus(SignetStatusEnums.UNENABLED.getCode());
                }
                //判断当前时间是否在有效开始时间和结束时间之间，修改印章状态为启用
                if ((voToEntity.getSealStartDate().after(date) && voToEntity.getSealEndDate().before(date)) || (voToEntity.getSealStartDate().equals(date))|| (voToEntity.getSealEndDate().equals(date))){
                    voToEntity.setSealStatus(SignetStatusEnums.ENABLED.getCode());
                }
            }
            contractSignetMapper.updateById(voToEntity);
            return voToEntity.getId();
        }

        voToEntity.setSealStatus(1);
        contractSignetMapper.insert(voToEntity);
        return voToEntity.getId();
    }

    private void verifyCodeAndName(SignetCreateReqVO vo) {
        // 校验名称是否重复，排除当前记录
        LambdaQueryWrapper<ContractSignetDO> nameWrapper = new LambdaQueryWrapper<>();
        nameWrapper.eq(ContractSignetDO::getSealName, vo.getSealName())
                .ne(vo.getId() != null, ContractSignetDO::getId, vo.getId());
        Long countName = contractSignetMapper.selectCount(nameWrapper);
        if (countName > 0) {
            throw exception(ErrorCodeConstants.NAME_EXISTS);
        }

        // 校验编码是否重复，排除当前记录
        LambdaQueryWrapper<ContractSignetDO> codeWrapper = new LambdaQueryWrapper<>();
        codeWrapper.eq(ContractSignetDO::getSealCode, vo.getSealCode())
                .ne(vo.getId() != null, ContractSignetDO::getId, vo.getId());
        Long countCode = contractSignetMapper.selectCount(codeWrapper);
        if (countCode > 0) {
            throw exception(ErrorCodeConstants.CODE_EXISTS);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DataPermission(enable = false)
    public PageResult<SignetPageRespVO> getSignetList(SignetPageReqVO vo) {
        //获取当前用户的公司id
        Long companyId = getCompanyId();
        LambdaQueryWrapperX<ContractSignetDO> query = new LambdaQueryWrapperX<>();
        query.likeIfPresent(ContractSignetDO::getSealName, vo.getSealName())
                .eqIfPresent(ContractSignetDO::getSealStatus, vo.getSealStatus())
                .eq(ContractSignetDO::getCompanyId, companyId)
                .orderByDesc(ContractSignetDO::getUpdateTime);
        PageResult<ContractSignetDO> contractSignetDOPageResult = contractSignetMapper.selectPage(vo, query);
        //将已过期的印章状态修改为已过期
        if (ObjectUtil.isNotEmpty(contractSignetDOPageResult.getList())) {
            //判断印章是否长期有效
            List<ContractSignetDO> contractSignetDOList = contractSignetDOPageResult.getList()
                    .stream().filter(item -> item.getIsPermanent() == 0).collect(Collectors.toList());
            //当前时间和印章有效期结束时间比较
            if (ObjectUtil.isNotEmpty(contractSignetDOList)) {
                List<ContractSignetDO> contractSignetDOS = new ArrayList<>();
                contractSignetDOList.forEach(item -> {
                    Date now = new Date();
                    if (ObjectUtil.isNotEmpty(item.getSealEndDate())) {
                        if (now.after(item.getSealEndDate())) {
                            item.setSealStatus(SignetStatusEnums.EXPIRED.getCode());
                            contractSignetDOS.add(item);
                        }
                    }
                });
                if (ObjectUtil.isNotEmpty(contractSignetDOS)) {
                    contractSignetMapper.updateBatch(contractSignetDOS);
                }
            }
        }
        PageResult<SignetPageRespVO> pageResult = SignetConverter.INSTANCE.toPageResult(contractSignetDOPageResult);
        pageResult.getList().forEach(item -> {
            item.setSealStatusName(SignetStatusEnums.getDesc(item.getSealStatus()));
            if (ObjectUtil.isNotEmpty(ActivityConfigurationEnum.getInstanceByDefKey(item.getSealProcess()))) {
                item.setSealProcessName(ActivityConfigurationEnum.getInstanceByDefKey(item.getSealProcess()).getName());
            }
        });

        return pageResult;
    }

    @Override
    @DataPermission(enable = false)
    public SignetDetailsRespVO getSignetDetails(SignetDetailsReqVO vo) throws Exception {
        //获取用户公司id
        Long companyId = getCompanyId();
        ContractSignetDO contractSignetDO = contractSignetMapper.selectOne(new LambdaQueryWrapperX<ContractSignetDO>()
                .eq(ContractSignetDO::getId, vo.getSealId()).eq(ContractSignetDO::getCompanyId, companyId));
        if (ObjectUtil.isNotEmpty(contractSignetDO)) {
            SignetDetailsRespVO signetDetailsRespVO = SignetConverter.INSTANCE.doToRespVO(contractSignetDO);
            if (ObjectUtil.isNotEmpty(ActivityConfigurationEnum.getInstanceByDefKey(signetDetailsRespVO.getSealProcess()))) {
                signetDetailsRespVO.setSealProcessName(ActivityConfigurationEnum.getInstanceByDefKey(signetDetailsRespVO.getSealProcess()).getName());
            }
            //印章图片地址
            signetDetailsRespVO.setSealPictureUrl(fileApi.getURL(contractSignetDO.getSealPictureId()));
            //印章规则信息
            ContractSignetSpecsDO contractSignetSpecsDO = contractSignetSpecsMapper.selectById(contractSignetDO.getSpecsId());
            signetDetailsRespVO.setShape(contractSignetSpecsDO.getShape());
            signetDetailsRespVO.setHigh(contractSignetSpecsDO.getHigh());
            signetDetailsRespVO.setWidth(contractSignetSpecsDO.getWidth());
            signetDetailsRespVO.setCode(contractSignetSpecsDO.getCode());
            return signetDetailsRespVO;
        }
        return null;
    }

    @Override
    public void updateSignetStatus(SignetDetailsReqVO vo) {
        //获取印章状态
        ContractSignetDO contractSignetDO = contractSignetMapper.selectById(vo.getSealId());
        //已启用的只能停用
        if (contractSignetDO.getSealStatus().equals(SignetStatusEnums.ENABLED.getCode())) {
            if (vo.getSealStatus().equals(SignetStatusEnums.DEACTIVATED.getCode())) {
                contractSignetMapper.update(null, new LambdaUpdateWrapper<ContractSignetDO>().set(ContractSignetDO::getSealStatus, vo.getSealStatus()).eq(ContractSignetDO::getId, vo.getSealId()));
            } else {
                throw exception(ErrorCodeConstants.SEAL_ENABLED_ERROR);
            }
            //已停用的只能启用
        } else if (contractSignetDO.getSealStatus().equals(SignetStatusEnums.DEACTIVATED.getCode())) {
            if (vo.getSealStatus().equals(SignetStatusEnums.ENABLED.getCode())) {
                contractSignetMapper.update(null, new LambdaUpdateWrapper<ContractSignetDO>().set(ContractSignetDO::getSealStatus, vo.getSealStatus()).eq(ContractSignetDO::getId, vo.getSealId()));
            } else {
                throw exception(ErrorCodeConstants.SEAL_DISABLED_ERROR);
            }
            //过期的无法修改
        } else if (contractSignetDO.getSealStatus().equals(SignetStatusEnums.EXPIRED.getCode())) {
            throw exception(ErrorCodeConstants.SEAL_EXPIRE_ERROR);
        } else {
            throw exception(ErrorCodeConstants.SEAL_UPDATE_STATUS_ERROR);
        }

    }


    @Override
    public List<SignetSpecsVO> getSignetSpecs() {
        return SignetConverter.INSTANCE.toSignetSpecsVO(contractSignetSpecsMapper.selectList());
    }

    @Override
    public List<SignetTypeVO> getSignetType() {
        return SignetConverter.INSTANCE.toSignetTypeVO(contractSignetTypeMapper.selectList());
    }

    @Override
    public List<SealProcessRespVO> getSignetProcessList() {
        List<Model> models = repositoryService.createModelQuery().modelTenantId(TenantContextHolder.getTenantId().toString())
                .orderByCreateTime()
                .desc()
                .list();
        return models.stream()
                .filter(model -> model.getName().contains("用印")) // 添加过滤条件
                .map(model -> {
                    SealProcessRespVO vo = new SealProcessRespVO();
                    vo.setId(model.getId());
                    vo.setName(model.getName());
                    vo.setKey(model.getKey());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcess(Long loginUserId, SealBpmReqVO reqVO) {
        //检测是否有在途流程
        if (bpmContractSignetMapper.selectCount(
                new LambdaQueryWrapper<BpmContractSignetDO>()
                        .eq(BpmContractSignetDO::getSignetId, reqVO.getSealId())
                        .eq(BpmContractSignetDO::getBusinessId, reqVO.getContractId())
                        .eq(BpmContractSignetDO::getResult, 1)) > 0) {
            throw exception(ErrorCodeConstants.SYSTEM_ERROR, "已发起用印申请，请勿重复提交");
        }
        ContractSignetDO contractSignetDO = contractSignetMapper.selectById(reqVO.getSealId());
        //发起流程
        BpmProcessInstanceCreateReqDTO bpmProcessInstanceCreateReqDTO = new BpmProcessInstanceCreateReqDTO();
        Long sealAdminId = contractSignetDO.getSealAdminId();
        Map<String, List<Long>> startUserSelectAssignees = new HashMap<String, List<Long>>();
        List<Long> sealAdminIdList = new ArrayList<>();
        if (sealAdminId != null) {
            sealAdminIdList.add(sealAdminId);
        }
        startUserSelectAssignees.put(FlowableModelEnums.SECOND_LEVEL.getKey(), sealAdminIdList);
        bpmProcessInstanceCreateReqDTO.setProcessDefinitionKey(ActivityConfigurationEnum.SEAL_APPLICATION_APPROVE.getDefinitionKey()).setBusinessKey(reqVO.getId()).setStartUserSelectAssignees(startUserSelectAssignees);
        //流程实例id
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), bpmProcessInstanceCreateReqDTO);
        BpmContractSignetDO bpmContractSignetDO = new BpmContractSignetDO()
                .setId(reqVO.getId()).setProcessInstanceId(processInstanceId).setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        bpmContractSignetMapper.updateById(bpmContractSignetDO);
        //修改合同状态为用印审批中
        contractMapper.updateById(new ContractDO().setId(reqVO.getContractId()).setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_SEAL_APPROVAL.getCode()));
        return processInstanceId;
    }

    @Override
    @DataPermission(enable = false)
    public SignetManageRespVO getSignetManage(String id) {
        //获取用户公司id
        Long companyId = getCompanyId();
        SignetManageRespVO manageRespVO = new SignetManageRespVO();
        BpmContractSignetDO bpmContractSignetDO = bpmContractSignetMapper.selectOne(new LambdaQueryWrapper<BpmContractSignetDO>().eq(BpmContractSignetDO::getId, id).eq(BpmContractSignetDO::getCompanyId, companyId));
        if (ObjectUtil.isNotEmpty(bpmContractSignetDO)){
            manageRespVO = SignetConverter.INSTANCE.toManageRespVO(bpmContractSignetDO);
            //获取印章信息
            ContractSignetDO contractSignetDO = contractSignetMapper.selectById(bpmContractSignetDO.getSignetId());
            if (ObjectUtil.isNotEmpty(contractSignetDO)) {
                manageRespVO.setSealCode(contractSignetDO.getSealCode());
                manageRespVO.setSealAdminId(contractSignetDO.getSealAdminId());
                manageRespVO.setSealAdminIdName(contractSignetDO.getSealAdminName());
                AdminUserRespDTO creatorName = adminUserApi.getUser(Long.valueOf(contractSignetDO.getCreator()));
                if (ObjectUtil.isNotEmpty(creatorName)) {
                    manageRespVO.setCreatorName(creatorName.getNickname());
                }
                manageRespVO.setSealPictureId(contractSignetDO.getSealPictureId());
                manageRespVO.setSealPictureUrl(contractSignetDO.getSealPictureUrl());
                DeptRespDTO dept = deptApi.getDept(bpmContractSignetDO.getDeptId());
                if (ObjectUtil.isNotEmpty(dept)) {
                    manageRespVO.setDeptName(dept.getName());
                }
                //获取合同信息
                ContractDO contractDO = contractMapper.selectById(bpmContractSignetDO.getBusinessId());
                if (ObjectUtil.isEmpty(contractDO)) {
                    throw  exception(DIY_ERROR,"未找到该用印合同");
                }
                manageRespVO.setAmount(BigDecimal.valueOf(contractDO.getAmount()));
                //获取合同类型
                ContractType contractType = contractTypeMapper.selectById(contractDO.getContractType());
                if (ObjectUtil.isNotEmpty(contractType)) {
                    manageRespVO.setContractType(contractType.getName());
                }
                List<BusinessFileDO> businessFileDOList= businessFileMapper.selectList(new LambdaQueryWrapperX<BusinessFileDO>().eq(BusinessFileDO::getBusinessId,id));
                manageRespVO.setAttachmentList(businessFileDOList);
                enhanceSign(bpmContractSignetDO,contractDO,manageRespVO);
            }
        }
        return manageRespVO;
    }
@Resource
private EcontractOrgApi econtractOrgApi;
    private void enhanceSign(BpmContractSignetDO bpmContractSignetDO, ContractDO contractDO, SignetManageRespVO manageRespVO) {
        if(IfNumEnums.YES.getCode().equals(contractDO.getIsOffline())){
            manageRespVO.setSignType("线下签署");
        }else {
            manageRespVO.setSignType("线上签署");
        }

        List<BpmTaskRespDTO>  taskRespDTOS = bpmTaskApi.getTaskInfoListByProcessInstanceId(bpmContractSignetDO.getProcessInstanceId());
        if(CollectionUtil.isNotEmpty(taskRespDTOS)){
            BpmTaskRespDTO task = bpmTaskApi.getTaskRespDTO( taskRespDTOS.get(0).getTaskId());
            String assignee  = task.getAssignee();
            //用印时间
            if(ObjectUtil.isNotEmpty(task.getEndTime())){
            manageRespVO.setSignTime(Date.from(task.getEndTime().atZone(ZoneId.of("Asia/Shanghai")).toInstant()));
            }
            //签订人
            AdminUserRespDTO user = adminUserApi.getUser(Long.valueOf(bpmContractSignetDO.getCreator()));
            if(ObjectUtil.isNotEmpty(user)){
                manageRespVO.setSignerName(user.getNickname());
            }
        }
        //申请时间
        Date applyTime = Date.from(bpmContractSignetDO.getCreateTime().atZone(ZoneId.of("Asia/Shanghai")).toInstant());
        manageRespVO.setApplyTime(applyTime);


    }

    @Override
    @DataPermission(enable = false)
    public PageResult<SignetProcessPageRespVO> getBpmAllTaskPage(Long loginUserId, SealApplicationListBpmReqVO pageVO) {
        Long companyId = getCompanyId();
        pageVO.setCompanyId(companyId);
        // 查询所有任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.SEAL_APPLICATION_APPROVE.getDefinitionKey());
        if (CollectionUtil.isEmpty(processInstanceRelationInfoRespDTOList)) {
            return new PageResult<SignetProcessPageRespVO>();
        }
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<BpmContractSignetDO> result = bpmContractSignetMapper.selectBpmPage(pageVO);
        return enhanceBpmPage(result, instanceRelationInfoRespDTOMap);
    }

    private Long getCompanyId() {
        //获取用户公司id
        Long companyId = SecurityFrameworkUtils.getLoginUser().getCompanyId();
        return companyId;
    }

    @Override
    @DataPermission(enable = false)
    public PageResult<SignetProcessPageRespVO> getBpmDoneTaskPage(Long loginUserId, SealApplicationListBpmReqVO pageVO) {
        Long companyId = getCompanyId();
        pageVO.setCompanyId(companyId);
        Integer taskResult = TEMP_INTEGER;
        // 查询待办任务
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.SEAL_APPLICATION_APPROVE.getDefinitionKey(), taskResult);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<BpmContractSignetDO> doPageResult = bpmContractSignetMapper.selectBpmPage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    @DataPermission(enable = false)
    public PageResult<SignetProcessPageRespVO> getBpmToDoTaskPage(Long loginUserId, SealApplicationListBpmReqVO pageVO) {
        Long companyId = getCompanyId();
        pageVO.setCompanyId(companyId);
        // 查询待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.SEAL_APPLICATION_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<BpmContractSignetDO> doPageResult = bpmContractSignetMapper.selectBpmPage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    @DataPermission(enable = false)
    public List<SignetDetailsRespVO> getEnableSignetList() {
        //获取用户公司id
        Long companyId = getCompanyId();
        LambdaQueryWrapper<ContractSignetDO> wrapper = new LambdaQueryWrapperX<ContractSignetDO>()
                .eq(ContractSignetDO::getSealStatus, SignetStatusEnums.ENABLED.getCode()).eq(ContractSignetDO::getCompanyId, companyId)
                .or(q -> q.isNotNull(ContractSignetDO::getSealEndDate)
                        .gt(ContractSignetDO::getSealEndDate, new Date()));
        List<ContractSignetDO> contractSignetDOS = contractSignetMapper.selectList(wrapper);
        List<SignetDetailsRespVO> respVOList = SignetConverter.INSTANCE.toRespVOList(contractSignetDOS);
        return respVOList;
    }

    @Override
    public List<SignetListRespVO> getSignetsByContractId(String contractId) {

        // 根据合同id筛选印章   条件：用印申请审批通过，没超过有效时间（暂时不要），印章状态为启用的
        List<BpmContractSignetDO> bpmContractSignetDOS = bpmContractSignetMapper.selectList(BpmContractSignetDO::getBusinessId, contractId, BpmContractSignetDO::getResult, BpmProcessInstanceResultEnum.APPROVE.getResult());
        if (CollectionUtil.isNotEmpty(bpmContractSignetDOS)) {
            List<String> signetIds = bpmContractSignetDOS.stream().map(BpmContractSignetDO::getSignetId).collect(Collectors.toList());

            LambdaQueryWrapperX<ContractSignetDO> queryWrapper = new LambdaQueryWrapperX<>();
            queryWrapper.in(ContractSignetDO::getId, signetIds).eq(ContractSignetDO::getSealStatus, SignetStatusEnums.ENABLED.getCode());

            List<ContractSignetDO> contractSignetDOS = contractSignetMapper.selectList(queryWrapper);
            return BeanUtils.toBean(contractSignetDOS, SignetListRespVO.class);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(SealBpmReqVO reqVO) throws Exception {
        List<BusinessFileDO> businessFileDOS = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(reqVO.getId())) {
            //修改
            BpmContractSignetDO bpmContractSignetDO = SignetConverter.INSTANCE.sealBpmReqVOtoEntity(reqVO);
            businessFileMapper.deleteByBusinessId(bpmContractSignetDO.getId());
            if (ObjectUtil.isNotEmpty(reqVO.getAttachmentIds())) {
                reqVO.getAttachmentIds().forEach(item -> {
                    BusinessFileDO businessFileDO = new BusinessFileDO();
                    businessFileDO.setBusinessId(bpmContractSignetDO.getId()).setFileId(item.getFileId()).setFileName(item.getName());
                    businessFileDOS.add(businessFileDO);
                });
                businessFileMapper.insertBatch(businessFileDOS);
            }
            bpmContractSignetMapper.updateById(bpmContractSignetDO);
        } else {
            //新增
            ContractSignetDO contractSignetDO = contractSignetMapper.selectById(reqVO.getSealId());
            ContractDO contractDO = contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>()
                    .eq(ContractDO::getId, reqVO.getContractId()).select(ContractDO::getName, ContractDO::getCode));
            BpmContractSignetDO bpmContractSignetDO = SignetConverter.INSTANCE.sealBpmReqVOtoEntity(reqVO);
            bpmContractSignetDO.setBusinessName(contractDO.getName()).setBusinessCode(contractDO.getCode()).
                    setResult(BpmProcessInstanceResultEnum.DRAFT.getResult()).setCompanyId(contractSignetDO.getCompanyId());
            bpmContractSignetMapper.insert(bpmContractSignetDO);
            //附件
            List<AttachmentVO> attachmentIds = reqVO.getAttachmentIds();
            if (ObjectUtil.isNotEmpty(attachmentIds)) {
                attachmentIds.forEach(item -> {
                    BusinessFileDO businessFileDO = new BusinessFileDO();
                    businessFileDO.setBusinessId(bpmContractSignetDO.getId()).setFileId(item.getFileId()).setFileName(item.getName());
                    businessFileDOS.add(businessFileDO);
                });
                businessFileMapper.insertBatch(businessFileDOS);
            }
            reqVO.setId(bpmContractSignetDO.getId());
            //发起流程
            String processInstanceId = this.createProcess(getLoginUserId(), reqVO);
            //提交
            if (ObjectUtil.isNotEmpty(reqVO.getIsSubmit())) {
                //添加taskId
                if (ObjectUtil.isNotEmpty(processInstanceId)) {
                    List<String> processInstanceIds = Arrays.asList(processInstanceId);
                    List<BpmTaskAllInfoRespVO> instanceIds = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(getLoginUserId(), processInstanceIds);
                    if (ObjectUtil.isNotEmpty(instanceIds)) {
                        for (BpmTaskAllInfoRespVO bpmTaskRespDTO : instanceIds) {
                            BpmTaskApproveReqVO taskApproveReqVO = new BpmTaskApproveReqVO().setTaskId(bpmTaskRespDTO.getTaskId()).setReason(reqVO.getAdvice());
                            taskService.approveTask(getLoginUserId(), taskApproveReqVO);
                        }

                    }
                }
            }
            return bpmContractSignetDO.getId();
        }

        return null;
    }

    @Override
    public PageResult<SignetProcessPageRespVO> getSignetProcessPage(SealApplicationListBpmReqVO reqVO) {
        //获取公司id
        reqVO.setCompanyId(getCompanyId());
        PageResult<BpmContractSignetDO> doPageResult = bpmContractSignetMapper.getSignetProcessPage(reqVO);
        PageResult<SignetProcessPageRespVO> resultList = SignetConverter.INSTANCE.toPageResult2(doPageResult);
        //申请人
        List<Long> creatorList = doPageResult.getList().stream().map(BpmContractSignetDO::getCreator).map(Long::parseLong).collect(Collectors.toList());
        Map<Long, AdminUserRespDTO> creatorMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(creatorList)) {
            List<AdminUserRespDTO> creatorListRespDTO = adminUserApi.getUserList(creatorList);
            creatorMap = CollectionUtils.convertMap(creatorListRespDTO, AdminUserRespDTO::getId);
        }
        for (SignetProcessPageRespVO item : resultList.getList()) {
            item.setApplyUserName(creatorMap.get(item.getApplyUser()).getNickname());
            item.setResultName(BpmProcessInstanceResultEnum.getInstance(item.getResult()).getDesc());
        }
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectSignetProcess(RejectSignetProcessReqVO reqVO) {
        //将合同状态改为未签署
        contractMapper.updateById(new ContractDO().setId(reqVO.getContractId()).setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode()));
        //将流程驳回
        BpmTaskRejectReqDTO bpmTaskRejectReqDTO = new BpmTaskRejectReqDTO();
        bpmTaskRejectReqDTO.setId(reqVO.getTaskId());
        bpmTaskRejectReqDTO.setReason(reqVO.getReason());
        bpmTaskApi.rejectTask(getLoginUserId(), bpmTaskRejectReqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        bpmContractSignetMapper.deleteById(id);
        businessFileMapper.delete(new LambdaQueryWrapperX<BusinessFileDO>().eq(BusinessFileDO::getBusinessId, id));
    }

    @Override
    public void deleteSignet(String id) {
        contractSignetMapper.deleteById(id);
    }

    @Override
    public SignetAgentNumRespVO getAgentNum(SealApplicationListBpmReqVO pageVO) {
        SignetAgentNumRespVO signetAgentNumRespVO = new SignetAgentNumRespVO();
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList=new ArrayList<>();
        //政采代办数量
        Long companyId = getCompanyId();
        pageVO.setCompanyId(companyId);
        if (1==pageVO.getFlag()) {
            //1=我发起的
            Integer taskResult = TEMP_INTEGER;
            // 查询待办任务
            if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
                taskResult = pageVO.getTaskResult();
            }
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.SEAL_APPLICATION_APPROVE.getDefinitionKey(), taskResult);

        }else if (2==pageVO.getFlag()) {
            //2=草稿
            // 查询待办任务
             processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.SEAL_APPLICATION_APPROVE.getDefinitionKey());
        }
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        pageVO.setIsGov(IfNumEnums.YES.getCode());
        Long signetAgentNum = bpmContractSignetMapper.selectBpmSignetNum(pageVO);
        signetAgentNumRespVO.setOrgSignetAgentNum(signetAgentNum);

        //总数量---包括政采和非政采
        //输入参数处理
        //工作流执行人操作状态查询
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE)), SIGN);

        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        //（相对方逻辑）免租户
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
//                        pageVO.setInstanceIdList(processInstanceIds);

                        //转成产品的合同类型数据(非交易)
                        Long signetAgentAllNum = selectAffirmPage(processInstanceIds);
                signetAgentNumRespVO.setSignetAgentNum(signetAgentAllNum);

                    });
    });
      return signetAgentNumRespVO;
    }
    private Long selectAffirmPage(List<String> processInstanceIds) {
        //获取用户部门id
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        AdminUserRespDTO user = adminUserApi.getUser(loginUser.getId());
        // 相对方id
        MPJLambdaWrapper<ContractDO> mpjQueryWrapper = new MPJLambdaWrapper<ContractDO>();
        mpjQueryWrapper.eq(ContractDO::getStatus, ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode());
//        if (CollectionUtil.isNotEmpty(processInstanceIds)) {
//            mpjQueryWrapper.in(ContractDO::getProcessInstanceId, processInstanceIds);
//        }
        if (ObjectUtil.isNotEmpty(user)) {
            List<Relative> relatives = relativeMapper.selectList4Relative(user.getId());
            if (CollectionUtil.isNotEmpty(relatives)) {
                List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                List<String> contractIds = signatoryRelDOS.stream().map(SignatoryRelDO::getContractId).collect(Collectors.toList());
                mpjQueryWrapper.and(w -> {
                    w.eq("t.dept_id", user.getDeptId());
                    if (CollectionUtil.isNotEmpty(contractIds)) {
                        w.or().in(ContractDO::getId, contractIds);
                    }
                });
            } else {
                mpjQueryWrapper.and(w -> w.eq("t.dept_id", user.getDeptId()));
            }
        }
        return contractMapper.selectCount(mpjQueryWrapper);
    }
    private PageResult<SignetProcessPageRespVO> enhanceBpmPage(PageResult<BpmContractSignetDO> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {
        List<BpmContractSignetDO> doList = doPageResult.getList();
        List<SignetProcessPageRespVO> resultList = SignetConverter.INSTANCE.toSignetProcessPageRespList(doList);
        if (CollectionUtil.isNotEmpty(doList)) {
            //流程信息
            Map<String, BpmContractSignetDO> bpmDOMap = CollectionUtils.convertMap(doList, BpmContractSignetDO::getProcessInstanceId);
            List<String> instanceList = doList.stream().map(BpmContractSignetDO::getProcessInstanceId).collect(Collectors.toList());

            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> allTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Long loginUserId = getLoginUserId();
            List<BpmTaskAllInfoRespVO> originalTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            //待处理的任务
            Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> allInfoRespVOList;
            List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();


            if (CollectionUtil.isNotEmpty(instanceList)) {
//                originalTaskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(loginUserId, instanceList);
//                taskAllInfoRespVOList = EcontractUtil.distinctTask(originalTaskAllInfoRespVOList);
//                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
//
//                toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTimeByUserId(originalTaskAllInfoRespVOList, getLoginUserId());
//                toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                allInfoRespVOList = bpmTaskApi.getAllTaskIdByProcessInstanceIds(instanceList);
                allInfoRespVOList = EcontractUtil.distinctTask(allInfoRespVOList);
                allTaskMap = CollectionUtils.convertMap(allInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

            }
            //申请人
            List<Long> creatorList = doList.stream().map(BpmContractSignetDO::getCreator).map(Long::parseLong).collect(Collectors.toList());
            Map<Long, AdminUserRespDTO> creatorMap = new HashMap<>();
            if (ObjectUtil.isNotEmpty(creatorList)) {
                List<AdminUserRespDTO> creatorListRespDTO = adminUserApi.getUserList(creatorList);
                creatorMap = CollectionUtils.convertMap(creatorListRespDTO, AdminUserRespDTO::getId);
            }
            for (SignetProcessPageRespVO respVO : resultList) {
                respVO.setApplyUserName(creatorMap.get(respVO.getApplyUser()) != null ? creatorMap.get(respVO.getApplyUser()).getNickname() : null);
                if (ObjectUtil.isNotEmpty(taskMap)) {
                    respVO.setTaskId(taskMap.get(respVO.getProcessInstanceId()).getTaskId());
                }
                BpmContractSignetDO bpmDO = bpmDOMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(bpmDO)) {
                    //流程实例
                    respVO.setProcessInstanceId(bpmDO.getProcessInstanceId());
                    //流程状态
                    respVO.setResult(bpmDO.getResult());
                    respVO.setResultName(BpmProcessInstanceResultEnum.getInstance(bpmDO.getResult()).getDesc());
                    //流程任务
                    ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO = instanceRelationInfoRespDTOMap.get(bpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)) {
                        respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
                    }
                }

                //全部审批列表的审批状态(找不到当前登录人的待办任务，既是已办任务)
//                BpmTaskAllInfoRespVO toDoTask = toDoTaskMap.get(bpmDO.getProcessInstanceId());
//                if (ObjectUtil.isNotNull(toDoTask)) {
//                    if (loginUserId != null) {
//                        respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTask.getAssigneeUserId()));
//                    }
//                    respVO.setFlowableStatusName(FlowableStatusEnums.getInstance(respVO.getFlowableStatus()).getInfo());
//
//                }else {
//                    //审批状态(全部里)
//                    respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
//                    respVO.setFlowableStatusName(FlowableStatusEnums.getInstance(respVO.getFlowableStatus()).getInfo());
//                }
                if (ObjectUtil.isNotNull(respVO.getProcessInstanceId()) && ObjectUtil.isNotNull(allTaskMap.get(respVO.getProcessInstanceId()))) {
                    respVO.setNodeName(allTaskMap.get(respVO.getProcessInstanceId()).getName());
                }

//                //已审批任务的状态赋值（当前人对该审批的最近的一次操作结果）
//                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = instanceRelationInfoRespDTOMap.get(bpmDO.getProcessInstanceId());
//                if (ObjectUtil.isNotNull(infoRespDTO)) {
//                    respVO.setDoneTaskResult(infoRespDTO.getProcessResult());
//                    if (ObjectUtil.isNotEmpty(respVO.getDoneTaskResult())) {
//                        respVO.setDoneTaskResultName(BpmProcessInstanceResultEnum.getInstance(respVO.getDoneTaskResult()).getDesc());
//                    }
//                }
            }
            PageResult<SignetProcessPageRespVO> pageResult = new PageResult<SignetProcessPageRespVO>();
            pageResult.setList(resultList).setTotal(doPageResult.getTotal());
            return pageResult;
        }

        return new PageResult<SignetProcessPageRespVO>();
    }
}
