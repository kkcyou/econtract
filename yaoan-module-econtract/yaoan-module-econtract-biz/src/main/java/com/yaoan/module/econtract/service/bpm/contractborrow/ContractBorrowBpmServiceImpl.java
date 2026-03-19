package com.yaoan.module.econtract.service.bpm.contractborrow;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.bpm.activity.BpmActivityApi;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.*;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.BigContractBorrowRecordPageRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.BorrowRecordRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.ContractBorrowRecordPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.ContractBorrowRecordPageRespVO;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.AttachmentVO;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.ContractArchivesRespVO;
import com.yaoan.module.econtract.convert.borrow.BorrowContractConverter;
import com.yaoan.module.econtract.convert.bpm.borrow.ContractBorrowBpmConverter;
import com.yaoan.module.econtract.convert.config.SystemConfigDTOConverter;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.dal.dataobject.borrow.BorrowContractDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.borrow.BorrowContractMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contractborrow.ContractBorrowBpmMapper;
import com.yaoan.module.econtract.dal.mysql.businessfile.BusinessFileMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contractarchives.ContractArchivesMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.econtract.enums.ApproveTypeEnum;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.StatusConstants;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums;
import com.yaoan.module.econtract.service.contractarchives.ContractArchivesServiceImpl;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.flowable.FlowableUtil;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.dept.DeptApi;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyDeptRespDTO;
import com.yaoan.module.system.api.dict.DictDataApi;
import com.yaoan.module.system.api.dict.dto.DictDataRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.CONTRACT_ARCHIVES_NOT_EXISTS;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/8 21:32
 */
@Service
public class ContractBorrowBpmServiceImpl implements ContractBorrowBpmService {



    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractBorrowBpmMapper borrowBpmMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private ContractTypeMapper contractTypeMapper;

    @Resource
    private BorrowContractMapper borrowContractMapper;
    @Resource
    private BpmActivityApi bpmActivityApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private BusinessFileMapper businessFileMapper;
    @Resource
    private ContractArchivesMapper contractArchivesMapper;
    @Resource
    private DictDataApi dictDataApi;
    @Resource
    private DeptApi deptApi;

    /**
     * 发起借阅审批
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitContractBorrowApproveFlowable(Long loginUserId, ContractBorrowBpmSubmitCreateReqVO reqVO) {
        String contractBorrowBpmId = new String();
        //编辑删除申请以及关联批量合同
        if (ObjectUtil.isNotEmpty(reqVO.getId())) {
            if (ObjectUtil.isEmpty(borrowBpmMapper.selectById(reqVO.getId()))) {
                throw exception(ErrorCodeConstants.CONTRACT_BORROW_EDIT_ERROR);
            }
            String borrowId = borrowBpmMapper.selectById(reqVO.getId()).getBorrowId();
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("borrow_id", borrowId);
            borrowContractMapper.deleteByMap(columnMap);
            String uuid = IdUtil.fastSimpleUUID();
            ArrayList<BorrowContractDO> borrowContractDOList = new ArrayList<>();
            //插入借阅合同关系表
            if (CollectionUtil.isNotEmpty(reqVO.getBorrowBpmReqVOList())) {
                for (BorrowBpmReqVO borrowBpmReqVO : reqVO.getBorrowBpmReqVOList()) {
                    BorrowContractDO borrowContractDO = new BorrowContractDO()
                            .setBorrowId(uuid)
                            .setContractId(borrowBpmReqVO.getContractId())
                            .setSubmitTime(reqVO.getSubmitTime())
                            .setReturnTime(borrowBpmReqVO.getReturnTime());
                    borrowContractDOList.add(borrowContractDO);
                }
            }
            borrowContractMapper.insertBatch(borrowContractDOList);
            AdminUserRespDTO userRespDTO = userApi.getUser(loginUserId);
            //1.插入请求单
            ContractBorrowBpmDO contractBorrowBpmDO = new ContractBorrowBpmDO()
                    .setId(reqVO.getId())
                    .setReason(reqVO.getApproveIntroduction())
                    .setBorrowId(uuid)
                    .setSubmitTime(reqVO.getSubmitTime())
                    .setFileId(reqVO.getFileId())
                    .setFileName(reqVO.getFileName())
                    .setApproveType(ApproveTypeEnum.APPROVING.getCode())
                    .setApproveIntroduction(reqVO.getApproveIntroduction())
                    .setUserId(loginUserId)
                    .setSubmitterName(userRespDTO == null ? "" : userRespDTO.getNickname())
                    .setBorrowName(reqVO.getBorrowName());
            if (reqVO.getSubmit() == 1) {
                contractBorrowBpmDO.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
            }
            borrowBpmMapper.updateById(contractBorrowBpmDO);
            if (reqVO.getSubmit() == 1) {
                // 2 发起 BPM
                // 2.1 流程变量
                Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
                processInstanceVariables.put("borrowId", contractBorrowBpmDO.getBorrowId());
                contractBorrowBpmId = contractBorrowBpmDO.getId();
                // 2.2 流程实例id
                String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                        new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey())
                                .setVariables(processInstanceVariables).setBusinessKey(contractBorrowBpmId));

                borrowBpmMapper.updateById(new ContractBorrowBpmDO().setId(contractBorrowBpmId).setProcessInstanceId(processInstanceId));
            }
        } else {
            //新增
            String uuid = IdUtil.fastSimpleUUID();

            ArrayList<BorrowContractDO> borrowContractDOList = new ArrayList<>();
            //插入借阅合同关系表
            if (CollectionUtil.isNotEmpty(reqVO.getBorrowBpmReqVOList())) {
                for (BorrowBpmReqVO borrowBpmReqVO : reqVO.getBorrowBpmReqVOList()) {
                    BorrowContractDO borrowContractDO = new BorrowContractDO()
                            .setBorrowId(uuid)
                            .setContractId(borrowBpmReqVO.getContractId())
                            .setSubmitTime(reqVO.getSubmitTime())
                            .setReturnTime(borrowBpmReqVO.getReturnTime());
                    borrowContractDOList.add(borrowContractDO);
                }
            }
            borrowContractMapper.insertBatch(borrowContractDOList);

            AdminUserRespDTO userRespDTO = userApi.getUser(loginUserId);
            //1.插入请求单
            ContractBorrowBpmDO contractBorrowBpmDO = new ContractBorrowBpmDO()
                    .setReason(reqVO.getApproveIntroduction())
                    .setBorrowId(uuid)
                    .setSubmitTime(reqVO.getSubmitTime())
                    .setFileId(reqVO.getFileId())
                    .setFileName(reqVO.getFileName())
                    .setApproveType(ApproveTypeEnum.APPROVING.getCode())
                    .setApproveIntroduction(reqVO.getApproveIntroduction())
                    .setUserId(loginUserId)
                    .setSubmitterName(userRespDTO == null ? "" : userRespDTO.getNickname())
                    .setBorrowName(reqVO.getBorrowName());
            if (reqVO.getSubmit() == 1) {
                contractBorrowBpmDO.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
            }
            //插入合同名称
            List<BorrowBpmReqVO> borrowBpmReqVOList = reqVO.getBorrowBpmReqVOList();
            List<String> contractIds = borrowBpmReqVOList.stream().map(BorrowBpmReqVO::getContractId).collect(Collectors.toList());
            if(ObjectUtil.isNotEmpty(contractIds)){
                List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().in(ContractDO::getId, contractIds));
                if (CollectionUtil.isNotEmpty(contractDOS)){
                    contractBorrowBpmDO.setBorrowName(contractDOS.get(0).getName());
                }
            }
            borrowBpmMapper.insert(contractBorrowBpmDO);
            contractBorrowBpmId = contractBorrowBpmDO.getId();
            if (reqVO.getSubmit() == 1) {
                // 2 发起 BPM
                // 2.1 流程变量
                Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
                processInstanceVariables.put("borrowId", contractBorrowBpmDO.getBorrowId());
                contractBorrowBpmId = contractBorrowBpmDO.getId();
                // 2.2 流程实例id
                String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                        new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey())
                                .setVariables(processInstanceVariables).setBusinessKey(contractBorrowBpmId));

                borrowBpmMapper.updateById(new ContractBorrowBpmDO().setId(contractBorrowBpmId).setProcessInstanceId(processInstanceId));
            }
        }
        return contractBorrowBpmId;
    }

    /**
     * 发起借阅审批 - 档案
     */
    @Override
    public String submitArchiveApproveFlowable(Long loginUserId, ContractBorrowBpmSubmitCreateReqVO reqVO) {
        //校验是否有纸质合同
        ContractArchivesDO contractArchivesDO1 = contractArchivesMapper.selectOne(ContractArchivesDO::getId, reqVO.getArchiveId());
        //查询合同类型，获取借阅key
        String key = ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey();
        if(ObjectUtil.isNotEmpty(contractArchivesDO1)){
            ContractDO contractDO = contractMapper.selectOne(new LambdaQueryWrapper<ContractDO>()
                    .eq(ContractDO::getId, contractArchivesDO1.getContractId()).select(ContractDO::getContractType));
            if(ObjectUtil.isNotEmpty(contractDO)){
                ContractType contractType = contractTypeMapper.selectOne(new LambdaQueryWrapperX<ContractType>()
                        .eq(ContractType::getId, contractDO.getContractType()));
                if(ObjectUtil.isNotEmpty(contractType) && ObjectUtil.isNotEmpty(contractType.getBorrowProcess())){
                    key = contractType.getBorrowProcess();
                }
            }
        }
        if(ObjectUtil.isNotEmpty(contractArchivesDO1)){
            //有纸质文件
            if(contractArchivesDO1.getMedium().contains("1")){
                //校验纸质是否归还
                if(reqVO.getBorrowType().contains(1)){
                    List<ContractBorrowBpmDO> contractBorrowBpmDOS = borrowBpmMapper.selectList(ContractBorrowBpmDO::getArchiveId, reqVO.getArchiveId());
                    if(ObjectUtil.isNotEmpty(contractBorrowBpmDOS)){
                        contractBorrowBpmDOS.forEach(contractBorrowBpmDO -> {
                            if(contractBorrowBpmDO.getBorrowType().contains("1") && contractBorrowBpmDO.getIsReturn().equals(0)){
                                throw exception(ErrorCodeConstants.CONTRACT_ARCHIVES_PAPER_NO_RETURN);
                            }
                        });
                    }
                }
            }else {
                //无纸质文件,不可借阅纸质文件
                if(reqVO.getBorrowType().contains(1)){
                    throw exception(ErrorCodeConstants.CONTRACT_ARCHIVES__NO_PAPER);
                }
            }
        }
        String contractBorrowBpmId = new String();
        //编辑删除申请以及关联批量合同
        if (ObjectUtil.isNotEmpty(reqVO.getId())) {
            if (ObjectUtil.isEmpty(borrowBpmMapper.selectById(reqVO.getId()))) {
                throw exception(ErrorCodeConstants.CONTRACT_BORROW_EDIT_ERROR);
            }
            String borrowId = borrowBpmMapper.selectById(reqVO.getId()).getBorrowId();
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("borrow_id", borrowId);
//            borrowContractMapper.deleteByMap(columnMap);
            String uuid = IdUtil.fastSimpleUUID();
            ArrayList<BorrowContractDO> borrowContractDOList = new ArrayList<>();
            //插入借阅合同关系表
            if (CollectionUtil.isNotEmpty(reqVO.getBorrowBpmReqVOList())) {
                for (BorrowBpmReqVO borrowBpmReqVO : reqVO.getBorrowBpmReqVOList()) {
                    BorrowContractDO borrowContractDO = new BorrowContractDO()
                            .setBorrowId(uuid)
                            .setContractId(borrowBpmReqVO.getContractId())
                            .setSubmitTime(reqVO.getSubmitTime())
                            .setReturnTime(borrowBpmReqVO.getReturnTime());
                    borrowContractDOList.add(borrowContractDO);
                }
            }
//            borrowContractMapper.insertBatch(borrowContractDOList);
            AdminUserRespDTO userRespDTO = userApi.getUser(loginUserId);
            //1.插入请求单
            ContractBorrowBpmDO contractBorrowBpmDO = new ContractBorrowBpmDO()
                    .setId(reqVO.getId())
                    .setReason(reqVO.getApproveIntroduction())
                    .setBorrowId(uuid)
                    .setSubmitTime(reqVO.getSubmitTime())
                    .setFileId(reqVO.getFileId())
                    .setFileName(reqVO.getFileName())
                    .setApproveType(ApproveTypeEnum.APPROVING.getCode())
                    .setApproveIntroduction(reqVO.getApproveIntroduction())
                    .setUserId(loginUserId)
                    .setSubmitterName(userRespDTO == null ? "" : userRespDTO.getNickname())
                    .setBorrowName(reqVO.getBorrowName())
                    /**
                     * 档案借阅新内容
                     */
                    .setArchiveId(reqVO.getArchiveId())
                    .setBorrowType(reqVO.getBorrowType().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")))
                    .setBorrowPermission(reqVO.getBorrowPermission().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")))
                    .setReturnTime(reqVO.getReturnTime());
            if (reqVO.getSubmit() == 1) {
                contractBorrowBpmDO.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
            }
            borrowBpmMapper.updateById(contractBorrowBpmDO);
            if (reqVO.getSubmit() == 1) {
                // 2 发起 BPM
                // 2.1 流程变量
                Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
                processInstanceVariables.put("borrowId", contractBorrowBpmDO.getBorrowId());
                contractBorrowBpmId = contractBorrowBpmDO.getId();
                // 2.2 流程实例id
                String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                        new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(key)
                                .setVariables(processInstanceVariables).setBusinessKey(contractBorrowBpmId));

                borrowBpmMapper.updateById(new ContractBorrowBpmDO().setId(contractBorrowBpmId).setProcessInstanceId(processInstanceId));
            }
            //存入附件表
            if(ObjectUtil.isNotEmpty(reqVO.getFiles())){
                processAttachments(reqVO.getFiles(), contractBorrowBpmId);
            }

        } else {
            ContractArchivesDO contractArchivesDO = contractArchivesMapper.selectById(reqVO.getArchiveId());
            //如果是借阅纸质档案 判断纸质档案是否已归还
            reqVO.getBorrowType().forEach(i -> {
                if (i == 1) {
                    if (contractArchivesDO.getIsBorrow() == 1) {
                        throw exception(ErrorCodeConstants.SYSTEM_ERROR, "该档案纸质版未归还，无法再次借阅");
                    }
                }
            });
            //新增
            String uuid = IdUtil.fastSimpleUUID();

            ArrayList<BorrowContractDO> borrowContractDOList = new ArrayList<>();
            //插入借阅合同关系表
            if (CollectionUtil.isNotEmpty(reqVO.getBorrowBpmReqVOList())) {
                for (BorrowBpmReqVO borrowBpmReqVO : reqVO.getBorrowBpmReqVOList()) {
                    BorrowContractDO borrowContractDO = new BorrowContractDO()
                            .setBorrowId(uuid)
                            .setContractId(borrowBpmReqVO.getContractId())
                            .setSubmitTime(reqVO.getSubmitTime())
                            .setReturnTime(borrowBpmReqVO.getReturnTime());
                    borrowContractDOList.add(borrowContractDO);
                }
            }
            borrowContractMapper.insertBatch(borrowContractDOList);

            AdminUserRespDTO userRespDTO = userApi.getUser(loginUserId);
            //1.插入请求单
            ContractBorrowBpmDO contractBorrowBpmDO = new ContractBorrowBpmDO()
                    .setReason(reqVO.getApproveIntroduction())
                    .setBorrowId(uuid)
                    .setSubmitTime(reqVO.getSubmitTime())
                    .setFileId(reqVO.getFileId())
                    .setFileName(reqVO.getFileName())
                    .setApproveType(ApproveTypeEnum.APPROVING.getCode())
                    .setApproveIntroduction(reqVO.getApproveIntroduction())
                    .setUserId(loginUserId)
                    .setSubmitterName(userRespDTO == null ? "" : userRespDTO.getNickname())
                    .setBorrowName(reqVO.getBorrowName())
            /**
             * 档案新增内容
             */
                    .setArchiveId(reqVO.getArchiveId())
                    .setBorrowType(reqVO.getBorrowType().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")))
                    .setBorrowPermission(reqVO.getBorrowPermission() == null ? null :reqVO.getBorrowPermission().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")))
                    .setReturnTime(reqVO.getReturnTime());
                    if(reqVO.getBorrowType().contains(1)){
                        contractBorrowBpmDO.setIsReturn(0);
                    }
            if (reqVO.getSubmit() == 1) {
                contractBorrowBpmDO.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
            }
            //插入合同名称
//            List<BorrowBpmReqVO> borrowBpmReqVOList = reqVO.getBorrowBpmReqVOList();
//            List<String> contractIds = borrowBpmReqVOList.stream().map(BorrowBpmReqVO::getContractId).collect(Collectors.toList());
//            if(ObjectUtil.isNotEmpty(contractIds)){
//                List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().in(ContractDO::getId, contractIds));
//                contractBorrowBpmDO.setBorrowName(contractDOS.get(0).getName());
//            }
            //插入档案名称
            if (ObjectUtil.isNotEmpty(contractArchivesDO)) {
                contractBorrowBpmDO.setBorrowName(contractArchivesDO.getName());
            }
            borrowBpmMapper.insert(contractBorrowBpmDO);
            contractBorrowBpmId = contractBorrowBpmDO.getId();
            if (reqVO.getSubmit() == 1) {
                // 2 发起 BPM
                // 2.1 流程变量
                Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
                processInstanceVariables.put("borrowId", contractBorrowBpmDO.getBorrowId());
                contractBorrowBpmId = contractBorrowBpmDO.getId();
                // 2.2 流程实例id
                String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                        new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(key)
                                .setVariables(processInstanceVariables).setBusinessKey(contractBorrowBpmId));

                borrowBpmMapper.updateById(new ContractBorrowBpmDO().setId(contractBorrowBpmId).setProcessInstanceId(processInstanceId));
            }
            //存入附件表
            if(ObjectUtil.isNotEmpty(reqVO.getFiles())){
                processAttachments(reqVO.getFiles(), contractBorrowBpmId);
            }
        }
        return contractBorrowBpmId;
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



    @Override
    public void approve(Long loginUserId, String id) {
        ContractBorrowBpmDO contractBorrowBpmDO = borrowBpmMapper.selectById(id);
        // 2.1 流程变量
        Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
        processInstanceVariables.put("borrowId", contractBorrowBpmDO.getBorrowId());
        String contractBorrowBpmId = contractBorrowBpmDO.getId();
        // 2.2 流程实例id
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey())
                        .setVariables(processInstanceVariables).setBusinessKey(contractBorrowBpmId));

        borrowBpmMapper.updateById(new ContractBorrowBpmDO().setId(contractBorrowBpmId).setProcessInstanceId(processInstanceId).setResult(BpmProcessInstanceResultEnum.PROCESS.getResult()));
    }

    @Override
    public void delete(String id) {
        ContractBorrowBpmDO contractBorrowBpmDO = borrowBpmMapper.selectById(id);
        borrowBpmMapper.deleteById(id);
        String borrowId = contractBorrowBpmDO.getBorrowId();
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("borrow_id", borrowId);
        borrowContractMapper.deleteByMap(columnMap);
    }

    /**
     * 借阅审批列表展示
     */
    @Override
    public PageResult<ContractBorrowBpmPageRespVO> getBpmPage(ContractBorrowBpmPageReqVO reqVO) {
        // 查询当前用户指定流程定义key的流程实例信息 包含待办和已办 代办包含任务ID，已办信息包含最新一次执行结果。
        // Collections.singleton(PROCESS_KEY):返回一个不可变集合，该集合仅包含 PROCESS_KEY 作为其唯一元素。这个集合不能被修改，且大小固定为 1。.
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(getLoginUserId(), Collections.singleton(ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey()));

        if (CollectionUtil.isEmpty(allRelationProcessInstanceInfos)) {
            return new PageResult<>();
        }
        // 将所有info元素的流程实例id 筛选出来重组list
        List<String> instanceList = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 将所有当前用户相关的流程实例存入reqVO
        reqVO.setProcessInstanceIds(instanceList);
        //筛出相关流程DO，重组分页VO
        PageResult<ContractBorrowBpmDO> borrowBpmDOPageResult = borrowBpmMapper.selectPage(reqVO);
        //空值校验
        if (CollectionUtil.isEmpty(borrowBpmDOPageResult.getList())) {
            return new PageResult<>();
        }

        return enhanceApprovePage(borrowBpmDOPageResult, allRelationProcessInstanceInfos);
    }

    /**
     * 借阅审批列表展示增强
     */
    private PageResult<ContractBorrowBpmPageRespVO> enhanceApprovePage(PageResult<ContractBorrowBpmDO> result, List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos) {

        //将审批结果为 处理中 的流程实例找出来
        List<ContractProcessInstanceRelationInfoRespDTO> processingInstanceList = allRelationProcessInstanceInfos.stream()
                .filter(item -> item.getProcessResult()
                        .equals(BpmProcessInstanceResultEnum.PROCESS.getResult())).collect(Collectors.toList());
        // processInstanceId字段作为键，taskId字段作为对应的值。存储在processTaskMap中。
        Map<String, String> processTaskMap = CollectionUtils.convertMap(processingInstanceList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId, ContractProcessInstanceRelationInfoRespDTO::getTaskId);
        // 将用户找出来
        List<Long> userIds = result.getList().stream().map(ContractBorrowBpmDO::getUserId).collect(Collectors.toList());
        List<AdminUserRespDTO> userInfoList = userApi.getUserList(userIds);
        Map<Long, AdminUserRespDTO> userInfoMap = CollectionUtils.convertMap(userInfoList, AdminUserRespDTO::getId);

        List<ContractBorrowBpmPageRespVO> borrowRespVoList = new ArrayList<ContractBorrowBpmPageRespVO>();

        Long loginUserId = null;
        if (CollectionUtil.isNotEmpty(result.getList())) {
            loginUserId = result.getList().get(0).getUserId();
        }
        List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().eqIfPresent(ContractDO::getCreator, String.valueOf(loginUserId)));
        Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
        // 遍历用户
        result.getList().forEach(item -> {
            ContractBorrowBpmPageRespVO respVO = new ContractBorrowBpmPageRespVO();
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
            respVO.setId(item.getId());
            respVO.setSubmitTime(item.getSubmitTime());
            respVO.setCreateTime(item.getCreateTime());
            respVO.setUpdateTime(item.getUpdateTime());
            respVO.setProcessInstanceId(item.getProcessInstanceId());
            respVO.setTaskId(taskIdStr);
            respVO.setResult(item.getResult());
//            ContractDO contractDO = contractDOMap.get(item.getContractId());
//            if (contractDO != null) {
//                respVO.setCode(contractDO.getCode());
//                respVO.setName(contractDO.getName());
//            }
            borrowRespVoList.add(respVO);
        });

        PageResult<ContractBorrowBpmPageRespVO> respVOPageResult = new PageResult<ContractBorrowBpmPageRespVO>();
        respVOPageResult.setList(borrowRespVoList);
        respVOPageResult.setTotal(result.getTotal());

        return respVOPageResult;
    }

    /**
     * 合同变更-获取任务分页（0=全部 1=已审批 2=未审批）
     */
    @Override
    public BigContractBorrowRecordPageRespVO getBpmAllTaskPage(Long loginUserId, ContractBorrowBpmPageReqVO pageVO) {
        //查询合同类型表，取起草流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .eq(ContractType::getTypeStatus, 1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getBorrowProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        // 查询所有任务
        if(ObjectUtil.isNotEmpty(definitionKeys)){
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(definitionKeys.get(0),definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
        }else {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey());
        }
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractBorrowBpmDO> contractBorrowBpmDOPageResult = borrowBpmMapper.selectContractChangeApprovePage(pageVO);
        return enhanceBpmPage(contractBorrowBpmDOPageResult, instanceRelationInfoRespDTOMap);

    }

    @Override
    public BigContractBorrowRecordPageRespVO getBpmDoneTaskPage(Long loginUserId, ContractBorrowBpmPageReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        //查询合同类型表，取起草流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .eq(ContractType::getTypeStatus,1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getBorrowProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        //获得已处理任务数据(已过滤掉已取消的任务),可筛选审批状态
        if(ObjectUtil.isNotEmpty(definitionKeys)){
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(definitionKeys.get(0), taskResult,definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));

        }else{
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey(), taskResult);
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
        PageResult<ContractBorrowBpmDO> contractBorrowBpmDOPageResult = borrowBpmMapper.selectContractChangeApprovePage(pageVO);
        return enhanceBpmPage(contractBorrowBpmDOPageResult, instanceRelationInfoRespDTOMap);

    }

    @Override
    public BigContractBorrowRecordPageRespVO getBpmToDoTaskPage(Long loginUserId, ContractBorrowBpmPageReqVO pageVO) {
        //查询合同类型表，取起草流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .eq(ContractType::getTypeStatus,1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getBorrowProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        // 查询待办任务
        if(ObjectUtil.isNotEmpty(definitionKeys)){
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(definitionKeys.get(0),definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
        }else {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey());
        }
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractBorrowBpmDO> contractBorrowBpmDOPageResult = borrowBpmMapper.selectContractChangeApprovePage(pageVO);
        return enhanceBpmPage(contractBorrowBpmDOPageResult, instanceRelationInfoRespDTOMap);

    }

    @Override
    public BigContractBorrowRecordPageRespVO getBpmAllTaskPageArchive(Long loginUserId, ContractBorrowBpmPageReqVO pageVO) {
        // 查询所有任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractBorrowBpmDO> contractBorrowBpmDOPageResult = borrowBpmMapper.selectContractChangeApprovePage(pageVO);
        return enhanceBpmPage(contractBorrowBpmDOPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public BigContractBorrowRecordPageRespVO getBpmDoneTaskPageArchive(Long loginUserId, ContractBorrowBpmPageReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        //获得已处理任务数据
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey(), taskResult);
        //去除已取消的任务。
        processInstanceRelationInfoRespDTOList = EcontractUtil.clearRepealTask(processInstanceRelationInfoRespDTOList);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractBorrowBpmDO> contractBorrowBpmDOPageResult = borrowBpmMapper.selectContractChangeApprovePage(pageVO);
        return enhanceBpmPage(contractBorrowBpmDOPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public BigContractBorrowRecordPageRespVO getBpmToDoTaskPageArchive(Long loginUserId, ContractBorrowBpmPageReqVO pageVO) {
        // 查询待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractBorrowBpmDO> contractBorrowBpmDOPageResult = borrowBpmMapper.selectContractChangeApprovePage(pageVO);
        return enhanceBpmPage(contractBorrowBpmDOPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public Map<String, Long> getBorrowTypeCount() {
        List<ContractBorrowBpmDO> contractBorrowBpmDOS = borrowBpmMapper.selectList();
        Map<String, Long> resultMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(contractBorrowBpmDOS)) {
            resultMap = contractBorrowBpmDOS.stream().collect(
                    Collectors.groupingBy(
                            contract -> {
                                int result = contract.getResult();
                                if (result == -1) {
                                    return "已归还";
                                } else if (result == 1) {
                                    return "处理中";
                                } else if (result == 2) {
                                    return "借阅中";
                                } else if (result == 3 || result == 2) {
                                    return "已审批";
                                } else {
                                    return "未知状态";
                                }
                            },
                            Collectors.counting()
                    )
            );
        }
        return resultMap;
    }

    @Override
    public void returnArchive(BorrowReturnReqVO vo) {
        //把查到的result更新成-1  判断当前时间如果超过预计归还时间  填充到实际归还时间
        ContractBorrowBpmDO contractBorrowBpmDO = new ContractBorrowBpmDO().setId(vo.getId());
        contractBorrowBpmDO.setActualReturnTime(vo.getActualReturnTime());
        contractBorrowBpmDO.setReason(vo.getReason());
        contractBorrowBpmDO.setIsReturn(1);
        borrowBpmMapper.updateById(contractBorrowBpmDO);
        //档案表修改纸质借阅状态
        ContractArchivesDO contractArchivesDO = new ContractArchivesDO().setId(contractBorrowBpmDO.getArchiveId()).setIsBorrow(0);
        contractArchivesMapper.updateById(contractArchivesDO);
    }

    private BigContractBorrowRecordPageRespVO enhanceBpmPage(PageResult<ContractBorrowBpmDO> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {
        List<ContractBorrowBpmDO> contractBorrowBpmDOS = doPageResult.getList();
        List<ContractBorrowRecordPageRespVO> respVOList = ContractBorrowBpmConverter.INSTANCE.convertList(contractBorrowBpmDOS);
        if (CollectionUtil.isNotEmpty(contractBorrowBpmDOS)) {
            List<String> borrowIds = contractBorrowBpmDOS.stream().map(ContractBorrowBpmDO::getBorrowId).collect(Collectors.toList());
            Map<String, ContractBorrowBpmDO> stringContractBorrowBpmDOMap = CollectionUtils.convertMap(contractBorrowBpmDOS, ContractBorrowBpmDO::getId);
            //发起人信息map
            List<AdminUserRespDTO> userRespDTOList = userApi.getUserList();
            Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);

            //流程信息
            List<String> doIdList = contractBorrowBpmDOS.stream().map(ContractBorrowBpmDO::getId).collect(Collectors.toList());
            List<ContractBorrowBpmDO> bpmDOList = borrowBpmMapper.selectList(new LambdaQueryWrapperX<ContractBorrowBpmDO>()
                    .inIfPresent(ContractBorrowBpmDO::getId, doIdList));
            Map<String, ContractBorrowBpmDO> bpmDOMap = CollectionUtils.convertMap(bpmDOList, ContractBorrowBpmDO::getBorrowId);

            List<String> instanceList = bpmDOList.stream().map(ContractBorrowBpmDO::getProcessInstanceId).collect(Collectors.toList());

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
//            //获取合同名称
//            List<BorrowContractDO> borrowContractDOS = borrowContractMapper.selectList(new LambdaQueryWrapperX<BorrowContractDO>().in(BorrowContractDO::getBorrowId, borrowIds).select(BorrowContractDO::getBorrowId, BorrowContractDO::getContractId));
//           List<String> contractIds = borrowContractDOS.stream().map(BorrowContractDO::getContractId).collect(Collectors.toList());
//            Map<String,BorrowContractDO> borrowContractDOMap = CollectionUtils.convertMap(borrowContractDOS, BorrowContractDO::getBorrowId);
//            List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().in(ContractDO::getId, contractIds).select(ContractDO::getId, ContractDO::getName));
//            Map<String,ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
            /**
             * 档案借阅补充
             */
            Map<String, ContractArchivesDO> contractArchivesDOMap = null;
            List<String> archiveIdList = respVOList.stream().map(ContractBorrowRecordPageRespVO::getArchiveId).filter(ObjectUtil::isNotNull).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(archiveIdList)) {
                List<ContractArchivesDO> contractArchivesDOS = contractArchivesMapper.selectBatchIds(archiveIdList);
                if (CollectionUtil.isNotEmpty(contractArchivesDOS)) {
                    contractArchivesDOMap = CollectionUtils.convertMap(contractArchivesDOS, ContractArchivesDO::getId);
                }
            }
            Result res = getResult();
            for (ContractBorrowRecordPageRespVO respVO : respVOList) {
//                BorrowContractDO borrowContract = borrowContractDOMap.get(respVO.getBorrowId());
//                if (borrowContract != null) {
//                    String contractId = borrowContract.getContractId();
//                    if (contractId != null) {
//                        Optional.ofNullable(contractDOMap.get(contractId))
//                                .map(ContractDO::getName).ifPresent(respVO::setContractName);
//                    }
//                }
                /**
                 * 档案借阅补充
                 */
                if (CollectionUtil.isNotEmpty(contractArchivesDOMap)){
                    ContractArchivesDO contractArchivesDO = contractArchivesDOMap.get(respVO.getArchiveId());
                    if (contractArchivesDO != null) {
                        respVO.setArchiveName(contractArchivesDO.getName());
                        respVO.setArchiveNo(contractArchivesDO.getCode());
                        respVO.setProCode(contractArchivesDO.getProCode());
                        respVO.setProName(contractArchivesDO.getProName());
                        respVO.setFondsNo(contractArchivesDO.getFondsNo());
                        respVO.setFirstLevelNo(contractArchivesDO.getFirstLevelNo());
                        respVO.setSecondLevelNo(contractArchivesDO.getSecondLevelNo());
                        respVO.setVolumeNo(contractArchivesDO.getVolumeNo());
                        respVO.setContractId(contractArchivesDO.getContractId());

                        if (ObjectUtil.isNotEmpty(contractArchivesDO.getMedium())){
                            respVO.setMedium(contractArchivesDO.getMedium());
                            if(contractArchivesDO.getMedium().contains(",")){
                                Map<String, DictDataRespDTO> map3 = res.map3;
                                String concatenatedValues = map3.values().stream()
                                        .map(DictDataRespDTO::getLabel)  // 获取每个 DictDataRespDTO 对象的 value 字段
                                        .collect(Collectors.joining(","));
                                respVO.setMediumName(concatenatedValues);
                            }else{
                                respVO.setMediumName(res.map3.get(contractArchivesDO.getMedium()).getLabel());
                            }
                        }
                    }
                }

                ContractBorrowBpmDO contractBorrowBpmDO = stringContractBorrowBpmDOMap.get(respVO.getId());
                if (ObjectUtil.isNotNull(contractBorrowBpmDO)) {
                    //最新审批时间
                    respVO.setApproveTime(contractBorrowBpmDO.getUpdateTime());
                    //流程实例
                    respVO.setProcessInstanceId(contractBorrowBpmDO.getProcessInstanceId());
                    //流程任务
                    ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO = instanceRelationInfoRespDTOMap.get(contractBorrowBpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)) {
                        respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
                    }
                    //提交人
                    AdminUserRespDTO userRespDTO = userRespDTOMap.get(Long.valueOf(contractBorrowBpmDO.getCreator()));
                    if (ObjectUtil.isNotNull(userRespDTO)) {
                        respVO.setSubmitter(userRespDTO.getNickname());
                        respVO.setBorrower(respVO.getSubmitter());
                    }
                    //流程状态
                    respVO.setResult(contractBorrowBpmDO.getResult());
                    //审批状态的三个字段
                    //历史任务信息
                    BpmTaskAllInfoRespVO historyTask = taskEndTimeMap.get(contractBorrowBpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(historyTask)) {
                        respVO.setHisTaskResult(historyTask.getResult());
                    }
                    //全部 审批列表的审批状态(找不到当前登录人的待办任务，既是已办任务)
                    BpmTaskAllInfoRespVO toDoTask = toDoTaskMap.get(contractBorrowBpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(toDoTask)) {
                        respVO.setAssigneeId(toDoTask.getAssigneeUserId());
                        respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTask.getAssigneeUserId()));
                        respVO.setFlowableStatusName(FlowableStatusEnums.getInstance(respVO.getFlowableStatus()).getInfo());
                    }
                    //审批状态
                    else {
                        respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
                        respVO.setFlowableStatusName(FlowableStatusEnums.DONE.getInfo());
                    }
                    //已审批任务的状态赋值（当前人对该审批的最近的一次操作结果）
                    ContractProcessInstanceRelationInfoRespDTO infoRespDTO = instanceRelationInfoRespDTOMap.get(contractBorrowBpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(infoRespDTO)) {
                        respVO.setDoneTaskResult(infoRespDTO.getProcessResult());
                    }
                }
            }
            PageResult<ContractBorrowRecordPageRespVO> pageResult = new PageResult<ContractBorrowRecordPageRespVO>();
            pageResult.setList(respVOList).setTotal(doPageResult.getTotal());
            BigContractBorrowRecordPageRespVO respVO = new BigContractBorrowRecordPageRespVO().setPageResult(pageResult);
            //获取配置
            respVO.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE)));
            return respVO;
        }
        BigContractBorrowRecordPageRespVO result = new BigContractBorrowRecordPageRespVO()
                .setPageResult(new PageResult<ContractBorrowRecordPageRespVO>()
                        .setList(Collections.emptyList())
                        .setTotal(doPageResult.getTotal())
                );
        result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE)));
        return result;
    }

    /**
     * 借阅记录列表展示
     */
    @Override
    public PageResult<BorrowRecordRespVO> getBorrowRecordPage(ContractBorrowRecordPageReqVO reqVO) {
        if(ObjectUtil.isNotEmpty(reqVO.getApplicantName())){
            List<AdminUserRespDTO> userListLikeNickname = userApi.getUserListLikeNickname(reqVO.getApplicantName());
            if(ObjectUtil.isNotEmpty(userListLikeNickname)){
                List<Long> ids = userListLikeNickname.stream().map(AdminUserRespDTO::getId).collect(Collectors.toList());
                reqVO.setApplicantIds(ids);
            }else{
                return new PageResult<>();
            }
        }
        //查询借阅列表
        PageResult<ContractBorrowBpmDO> doPageResult =  borrowBpmMapper.selectBorrowRecordPage(reqVO,null);
        PageResult<BorrowRecordRespVO> result = ContractBorrowBpmConverter.INSTANCE.convertPage2(doPageResult);
        //查询存在的档案信息
        List<String> archiveIdS = result.getList().stream()
                .map(BorrowRecordRespVO::getArchiveId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(archiveIdS)) {
            return new PageResult<>();
        }
        List<ContractArchivesDO> archivesDOS = contractArchivesMapper.selectList(ContractArchivesDO::getId, archiveIdS);
        Map<String, ContractArchivesDO> archivesDOMap = archivesDOS.stream().collect(Collectors.toMap(ContractArchivesDO::getId, Function.identity()));
        if (CollectionUtil.isNotEmpty(archivesDOMap)) {
            Result res = getResult();
            for (BorrowRecordRespVO borrowRecordRespVO : result.getList()) {
                ContractArchivesDO archivesDO = archivesDOMap.get(borrowRecordRespVO.getArchiveId());
                // 填充档案信息
                if (ObjectUtil.isNotNull(archivesDO)) {
                    borrowRecordRespVO.setName(archivesDO.getName());
//                    borrowRecordRespVO.setCode(archivesDO.getCode());
//                    borrowRecordRespVO.setFondsNo(archivesDO.getFondsNo());
//                    borrowRecordRespVO.setFirstLevelNo(archivesDO.getFirstLevelNo());
//                    borrowRecordRespVO.setSecondLevelNo(archivesDO.getSecondLevelNo());
//                    borrowRecordRespVO.setVolumeNo(archivesDO.getVolumeNo());
                    borrowRecordRespVO.setProCode(archivesDO.getProCode());
                    borrowRecordRespVO.setProName(archivesDO.getProName());
                    borrowRecordRespVO.setContractId(archivesDO.getContractId());
                    if (ObjectUtil.isNotEmpty(archivesDO.getMedium())){
                        borrowRecordRespVO.setMedium(archivesDO.getMedium());
                        if(archivesDO.getMedium().contains(",")){
                            Map<String, DictDataRespDTO> map3 = res.map3;
                            String concatenatedValues = map3.values().stream()
                                    .map(DictDataRespDTO::getLabel)  // 获取每个 DictDataRespDTO 对象的 value 字段
                                    .collect(Collectors.joining(","));
                            borrowRecordRespVO.setMediumName(concatenatedValues);
                        }else{
                            borrowRecordRespVO.setMediumName(res.map3.get(archivesDO.getMedium()).getLabel());
                        }
                    }
                }
                // 获取当前时间
                Date date = new Date();
                Date returnTime = borrowRecordRespVO.getReturnTime();
                Date submitTime = borrowRecordRespVO.getSubmitTime();
                if(borrowRecordRespVO.getResult().equals(2)){
                    if (submitTime != null && submitTime.after(date)) {
                        if(borrowRecordRespVO.getBorrowType().contains("1")){
                            borrowRecordRespVO.setBorrowStatusName("借阅中");
                        }else {
                            borrowRecordRespVO.setBorrowStatusName("待取档");
                        }

                    } else if (returnTime != null && returnTime.after(date)) {
                        if(ObjectUtil.isNotEmpty(borrowRecordRespVO.getIsReturn())){
                            if(borrowRecordRespVO.getIsReturn()==0){
                                // 计算剩余时间
                                Instant returnInstant = returnTime.toInstant();
                                Instant submitInstant = date.toInstant();
                                Duration duration = Duration.between(submitInstant, returnInstant);
                                long remainTimeInSeconds = duration.getSeconds();
                                long days = remainTimeInSeconds / (24 * 3600);
                                long hours = (remainTimeInSeconds % (24 * 3600)) / 3600;
                                borrowRecordRespVO.setRemainTime(days + "天" + hours + "小时");
                                borrowRecordRespVO.setBorrowStatusName("借阅中");
                            } else if(borrowRecordRespVO.getIsReturn()==1){
                                borrowRecordRespVO.setBorrowStatusName("已归还");
                            }
                        } else {
                            // 计算剩余时间
                            Instant returnInstant = returnTime.toInstant();
                            Instant submitInstant = date.toInstant();
                            Duration duration = Duration.between(submitInstant, returnInstant);
                            long remainTimeInSeconds = duration.getSeconds();
                            long days = remainTimeInSeconds / (24 * 3600);
                            long hours = (remainTimeInSeconds % (24 * 3600)) / 3600;
                            borrowRecordRespVO.setRemainTime(days + "天" + hours + "小时");
                            borrowRecordRespVO.setBorrowStatusName("借阅中");
                        }

                    } else {
                        if(ObjectUtil.isNotEmpty(borrowRecordRespVO.getIsReturn())){
                            if(borrowRecordRespVO.getIsReturn()==1){
                                borrowRecordRespVO.setBorrowStatusName("已归还");
                            }else if(borrowRecordRespVO.getBorrowType().contains("1") && borrowRecordRespVO.getIsReturn()==0){
                                borrowRecordRespVO.setBorrowStatusName("借阅中");
                            }
                        }
                        if(borrowRecordRespVO.getBorrowType().equals("0")){
                            borrowRecordRespVO.setBorrowStatusName("已归还");
                        }

                    }
                }else{
                    borrowRecordRespVO.setBorrowStatusName(CommonFlowableReqVOResultStatusEnums.getInstance(borrowRecordRespVO.getResult()).getInfo());
                }

            }
        }
        return result;
    }

    /**
     * 随机生成保密级别 1-3
     */
    private Integer getRandomLevel() {
        return new Random().nextInt(3) + 1;
    }

    /**
     * 借阅申请列表
     *
     * @param reqVO
     * @return
     */
    @Override
    public PageResult<ContractBorrowBpmPageRespVO> getBorrowPage(ContractBorrowBpmPageReqVO reqVO) {
        PageResult<ContractBorrowBpmDO> contractBorrowBpmDOPageResult = borrowBpmMapper.listApprovedTerm(reqVO);
        PageResult<ContractBorrowBpmPageRespVO> contractBorrowBpmPageRespVOPageResult = ContractBorrowBpmConverter.INSTANCE.convertPage(contractBorrowBpmDOPageResult);
        return enhancePage(contractBorrowBpmPageRespVOPageResult);
    }

    private PageResult<ContractBorrowBpmPageRespVO> enhancePage(PageResult<ContractBorrowBpmPageRespVO> contractBorrowBpmPageRespVOPageResult) {
        Long loginId = getLoginUserId();

        if (CollectionUtil.isNotEmpty(contractBorrowBpmPageRespVOPageResult.getList())) {
            List<String> instanceList = contractBorrowBpmPageRespVOPageResult.getList().stream().map(ContractBorrowBpmPageRespVO::getProcessInstanceId).collect(Collectors.toList());
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            //待处理的任务
            List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();


            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(loginId, instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                //待处理的任务
                toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTime(taskAllInfoRespVOList);
                toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }
            //用户信息
            List<AdminUserRespDTO> userList = adminUserApi.getUserList();
            Map<Long, AdminUserRespDTO> userMap = userList.stream().collect(Collectors.toMap(AdminUserRespDTO::getId, Function.identity()));


            for (ContractBorrowBpmPageRespVO respVO : contractBorrowBpmPageRespVOPageResult.getList()) {
                //如果是被退回状态，需要返回流程任务id
                BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
                if (ObjectUtil.isNotNull(resultEnum)) {
                    if (BpmProcessInstanceResultEnum.BACK == resultEnum) {
                        BpmTaskAllInfoRespVO task = taskMap.get(respVO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(task)) {
                            respVO.setTaskId(String.valueOf(task.getTaskId()));
                        }
                    }
                    //待办任务的被分配人
                    if (BpmProcessInstanceResultEnum.APPROVE != resultEnum) {
                        BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = toDoTaskMap.get(respVO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
                            respVO.setAssigneeId(bpmTaskAllInfoRespVO.getAssigneeUserId());
                        }
                    }
                }
                // 赋值流程状态（审核状态）
                BpmProcessInstanceResultEnum instance = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
                if (instance != null) {
                    respVO.setApproveStatus(instance.getDesc());
                }
                //申请人
                AdminUserRespDTO user = userMap.get(Long.valueOf(respVO.getCreator()));
                if (ObjectUtil.isNotNull(user)) {
                    respVO.setSubmitter(user.getNickname());
                }
                if(ObjectUtil.isEmpty(respVO.getBorrowName())){

                }

            }

        }
        return contractBorrowBpmPageRespVOPageResult;
    }

    @Override
    public BorrowRespVO getBorrowDetail(String id) {
        ContractBorrowBpmDO contractBorrowBpmDO = borrowBpmMapper.selectById(id);
        if(ObjectUtil.isEmpty(contractBorrowBpmDO)){
            return null;
        }
        BorrowRespVO borrowRespVO = ContractBorrowBpmConverter.INSTANCE.convertVO(contractBorrowBpmDO);
       //获取档案信息
        ContractArchivesDO contractArchivesDO = contractArchivesMapper.selectById(contractBorrowBpmDO.getArchiveId());
        borrowRespVO.setArchiveName(contractArchivesDO.getName());
        borrowRespVO.setArchiveCode(contractArchivesDO.getCode());

        if(ObjectUtil.isEmpty(contractArchivesDO)){
            throw exception(CONTRACT_ARCHIVES_NOT_EXISTS);
        }
        ContractArchivesRespVO contractArchivesRespVO = BeanUtils.toBean(contractArchivesDO, ContractArchivesRespVO.class);
        DeptRespDTO dept = deptApi.getDept(contractArchivesRespVO.getDeptId());
        if(ObjectUtil.isNotEmpty(dept)){
            contractArchivesRespVO.setDeptName(dept.getName());
        }
        borrowRespVO.setArchiveInfo(contractArchivesRespVO);
        //借阅类型
        List<DictDataRespDTO> archiveStorageYearList = dictDataApi.getDictDataList("borrowing_type");
        Map<String, DictDataRespDTO> map1 = CollectionUtils.convertMap(archiveStorageYearList, DictDataRespDTO::getValue);
        //电子文件权限
        List<DictDataRespDTO> openStatusNameList = dictDataApi.getDictDataList("efile_permissions");
        Map<String, DictDataRespDTO> map2 = CollectionUtils.convertMap(openStatusNameList, DictDataRespDTO::getValue);
        String[] parts = contractBorrowBpmDO.getBorrowType().split(",");
        StringBuilder borrowType = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            DictDataRespDTO dictDataRespDTO = map1.get(part);
            if (ObjectUtil.isNotNull(dictDataRespDTO)) {
                borrowType.append(dictDataRespDTO.getLabel());
                // 只有不是最后一个元素才加逗号
                if (i < parts.length - 1) {
                    borrowType.append(",");
                }
            }
        }
        borrowRespVO.setBorrowTypeStr(borrowType.toString());
        if(ObjectUtil.isNotEmpty(contractBorrowBpmDO.getBorrowPermission())){
            String[] borrowPermissions = contractBorrowBpmDO.getBorrowPermission().split(",");
            StringBuilder borrowPermission = new StringBuilder();
            for (int i = 0; i < borrowPermissions.length; i++) {
                String part = borrowPermissions[i];
                DictDataRespDTO dictDataRespDTO = map2.get(part);
                if (ObjectUtil.isNotNull(dictDataRespDTO)) {
                    borrowPermission.append(dictDataRespDTO.getLabel());
                    // 只有不是最后一个元素才加逗号
                    if (i < borrowPermissions.length - 1) {
                        borrowPermission.append(",");
                    }
                }
            }
            borrowRespVO.setBorrowPermissionStr(borrowPermission.toString());
        }
        List<BusinessFileDO> contractContentFileDOS = businessFileMapper.selectByBusinessId(id);
        if (ObjectUtil.isNotEmpty(contractContentFileDOS)) {
            List<AttachmentVO> files = convertToAttachmentVOList(contractContentFileDOS);
            borrowRespVO.setFiles(files);
        }
        return borrowRespVO;
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

    @Override
    public PageResult<ContractBorrowBpmPageRespVO> getBorrowList(ContractBorrowBpmPageReqVO reqVO) {
        //查询借阅记录
        PageResult<ContractBorrowBpmDO> contractBorrowBpmDOPageResult = borrowBpmMapper.listApprovedTerm(reqVO);
        PageResult<ContractBorrowBpmPageRespVO> contractBorrowBpmPageRespVOPageResult = ContractBorrowBpmConverter.INSTANCE.convertPage(contractBorrowBpmDOPageResult);
        PageResult<ContractBorrowBpmPageRespVO> result = enhancePage(contractBorrowBpmPageRespVOPageResult);
        //查询借阅合同
        PageResult<BorrowContractDO> borrowContractDOS = borrowContractMapper.selectContractPage2(reqVO);
        PageResult<BorrowRecordRespVO> borrowRecordRespVOPageResult = BorrowContractConverter.INSTANCE.convertPage(borrowContractDOS);
        //查询存在的合同信息
        List<String> contractIdS = borrowRecordRespVOPageResult.getList().stream()
                .map(BorrowRecordRespVO::getContractId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(contractIdS)) {
            return new PageResult<>();
        }
        List<ContractDO> contractDOList = contractMapper.selectList(ContractDO::getId, contractIdS);
        Map<String, ContractDO> contractDOMap = contractDOList.stream().collect(Collectors.toMap(ContractDO::getId, contractDO -> contractDO));
        //返回值添加合同名称和编码
        if (CollectionUtil.isNotEmpty(contractDOMap)) {
            for (BorrowRecordRespVO borrowRecordRespVO : borrowRecordRespVOPageResult.getList()) {
                ContractDO contractDO = contractDOMap.get(borrowRecordRespVO.getContractId());
                if (ObjectUtil.isNotNull(contractDO)) {
                    borrowRecordRespVO.setName(contractDO.getName());
//                    borrowRecordRespVO.setCode(contractDO.getCode());
                }
                //计算剩余时间
                //获取当前时间
                Date date = new Date();
                Date returnTime = borrowRecordRespVO.getReturnTime();
                Date submitTime = borrowRecordRespVO.getSubmitTime();
                if (submitTime.after(date)) {
                    borrowRecordRespVO.setRemainTime("待借阅");
                } else if (returnTime.after(date)) {
                    // 计算剩余时间
                    Instant returnInstant = returnTime.toInstant();
                    Instant submitInstant = date.toInstant();
                    Duration duration = Duration.between(submitInstant, returnInstant);
                    // 将剩余时间格式化为"X天X小时"的形式
                    long remainTimeInSeconds = duration.getSeconds();
                    long days = remainTimeInSeconds / (24 * 3600);  // 计算剩余天数
                    long hours = (remainTimeInSeconds % (24 * 3600)) / 3600;  // 计算剩余小时数
                    String formattedRemainTime = days + "天" + hours + "小时";
                    borrowRecordRespVO.setRemainTime(formattedRemainTime);
                }  else {
                    borrowRecordRespVO.setRemainTime("已过期");  // 如果归还时间在当前时间之前，将剩余时间设置为0
                }
            }
        }
        List<BorrowRecordRespVO> BorrowRecordList = borrowRecordRespVOPageResult.getList();
        List<ContractBorrowBpmPageRespVO> ContractBorrowBpLlist = result.getList();

        Map<String, List<BorrowRecordRespVO>> borrowRecordMap = BorrowRecordList.stream()
                .collect(Collectors.groupingBy(BorrowRecordRespVO::getBorrowId));

        ContractBorrowBpLlist.forEach(contractBorrowBpmPageRespVO -> {
            List<BorrowRecordRespVO> borrowRecordList = borrowRecordMap.get(contractBorrowBpmPageRespVO.getBorrowId());
            if (borrowRecordList != null) {
                contractBorrowBpmPageRespVO.setBorrowRecordRespVO(borrowRecordList);
            }
        });

        return result;
    }

    @Override
    @DataPermission(enable = false)
    public BorrowTypeCountRespVO getBorrowTypeCountV2() {
        //获取用户公司
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        //根据公司id获取下属部门
        List<DeptRespDTO> deptIds = deptApi.getDeptListByCompanyId(loginUser.getCompanyId());
        List<Long> deptIdList = deptIds.stream().map(DeptRespDTO::getId).collect(Collectors.toList());
        BorrowTypeCountRespVO borrowTypeCountRespVO = new BorrowTypeCountRespVO();
        MPJLambdaWrapper<ContractBorrowBpmDO> wrapperX = new MPJLambdaWrapper<ContractBorrowBpmDO>()
                .selectAll(ContractBorrowBpmDO.class);
        wrapperX.leftJoin(ContractArchivesDO.class, ContractArchivesDO::getId, ContractBorrowBpmDO::getArchiveId);
        wrapperX.select(ContractBorrowBpmDO::getId, ContractBorrowBpmDO::getBorrowType, ContractBorrowBpmDO::getIsReturn, ContractBorrowBpmDO::getReturnTime,
                        ContractBorrowBpmDO::getActualReturnTime, ContractBorrowBpmDO::getSubmitTime,ContractBorrowBpmDO::getResult)
                .isNotNull(ContractBorrowBpmDO::getArchiveId).in(ContractBorrowBpmDO::getDeptId, deptIdList);
        List<ContractBorrowBpmDO> contractBorrowBpmDOS = borrowBpmMapper.selectList(wrapperX);
        if(ObjectUtil.isNotEmpty(contractBorrowBpmDOS)){
            //档案借阅申请数
            borrowTypeCountRespVO.setArchiveCount(contractBorrowBpmDOS.size());
            //已审批
            borrowTypeCountRespVO.setDoneCount((int) contractBorrowBpmDOS.stream().filter(item->item.getResult()==2).count());
            //审批中
            borrowTypeCountRespVO.setDoingCount((int) contractBorrowBpmDOS.stream().filter(item->item.getResult()!=2).count());
            //借阅中
            int borrowCount = 0;
            //已归还
            int returnDoneCount = 0;
            Date date = new Date();
            for(ContractBorrowBpmDO item:contractBorrowBpmDOS){
               Date returnTime = item.getReturnTime();
               if(item.getResult().equals(2)){
                   //电子
                   if("0".equals(item.getBorrowType()) &&returnTime != null  && returnTime.after(date)){
                       borrowCount = borrowCount + 1;
                       //纸质
                   } else if(item.getBorrowType().contains("1") && item.getIsReturn() == 0){
                       borrowCount = borrowCount + 1;
                   }

                   if("0".equals(item.getBorrowType()) && ObjectUtil.isNotEmpty(returnTime) && returnTime.before(date)){
                       returnDoneCount = returnDoneCount + 1;
                   }else if(item.getBorrowType().contains("1") && item.getIsReturn() == 1){
                       returnDoneCount = returnDoneCount + 1;
                   }
               }
            }
            //已失效
            int returnFailCount = 0;
            //档案已归还数+档案已失效数（个）
            borrowTypeCountRespVO.setReturnCount(returnDoneCount + returnFailCount);
            borrowTypeCountRespVO.setBorrowCount(borrowCount);
            borrowTypeCountRespVO.setReturnDoneCount(returnDoneCount);
            borrowTypeCountRespVO.setReturnFailCount(returnFailCount);
            return  borrowTypeCountRespVO;
        }
        return null;
    }

    @Override
    @DataPermission(enable = false)
    public PageResult<BorrowRecordRespVO> borrowLedger(ContractBorrowRecordPageReqVO reqVO) {
        //获取用户公司
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        //根据公司id获取下属部门
        List<DeptRespDTO> deptIds = deptApi.getDeptListByCompanyId(loginUser.getCompanyId());
        List<Long> deptIdList = deptIds.stream().map(DeptRespDTO::getId).collect(Collectors.toList());
        if(ObjectUtil.isNotEmpty(reqVO.getApplicantName())){
            List<AdminUserRespDTO> userListLikeNickname = userApi.getUserListLikeNickname(reqVO.getApplicantName());
            if(ObjectUtil.isNotEmpty(userListLikeNickname)){
                List<Long> ids = userListLikeNickname.stream().map(AdminUserRespDTO::getId).collect(Collectors.toList());
                reqVO.setApplicantIds(ids);
            }else{
                return new PageResult<>();
            }
        }
        //查询借阅列表
        PageResult<ContractBorrowBpmDO> doPageResult =  borrowBpmMapper.selectBorrowRecordPage(reqVO,deptIdList);
        PageResult<BorrowRecordRespVO> result = ContractBorrowBpmConverter.INSTANCE.convertPage2(doPageResult);
        //查询存在的档案信息
        List<String> archiveIdS = result.getList().stream()
                .map(BorrowRecordRespVO::getArchiveId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(archiveIdS)) {
            return new PageResult<>();
        }
        List<ContractArchivesDO> archivesDOS = contractArchivesMapper.selectList(ContractArchivesDO::getId, archiveIdS);
        Map<String, ContractArchivesDO> archivesDOMap = archivesDOS.stream().collect(Collectors.toMap(ContractArchivesDO::getId, Function.identity()));
        if (CollectionUtil.isNotEmpty(archivesDOMap)) {
            for (BorrowRecordRespVO borrowRecordRespVO : result.getList()) {
                ContractArchivesDO archivesDO = archivesDOMap.get(borrowRecordRespVO.getArchiveId());
                // 填充档案信息
                if (ObjectUtil.isNotNull(archivesDO)) {
                    borrowRecordRespVO.setName(archivesDO.getName() != null? archivesDO.getName():null);
//                    borrowRecordRespVO.setCode(archivesDO.getCode() != null? archivesDO.getCode():null);
//                    borrowRecordRespVO.setFondsNo(archivesDO.getFondsNo() != null? archivesDO.getFondsNo():null);
//                    borrowRecordRespVO.setFirstLevelNo(archivesDO.getFirstLevelNo() != null? archivesDO.getFirstLevelNo():null);
//                    borrowRecordRespVO.setSecondLevelNo(archivesDO.getSecondLevelNo() != null? archivesDO.getSecondLevelNo():null);
//                    borrowRecordRespVO.setVolumeNo(archivesDO.getVolumeNo() != null? archivesDO.getVolumeNo():null);
                    borrowRecordRespVO.setProName(archivesDO.getProName() != null? archivesDO.getProName():null);
                    borrowRecordRespVO.setProCode(archivesDO.getProCode() != null? archivesDO.getProCode():null);
                    borrowRecordRespVO.setContractId(archivesDO.getContractId() != null? archivesDO.getContractId():null);
                }
                if(borrowRecordRespVO.getResult().equals(CommonFlowableReqVOResultStatusEnums.SUCCESS.getResultCode())){
                    // 获取当前时间
                    Date date = new Date();
                    Date returnTime = borrowRecordRespVO.getReturnTime();
                    Date submitTime = borrowRecordRespVO.getSubmitTime();
                    if (submitTime != null && submitTime.after(date)) {
                        if(borrowRecordRespVO.getBorrowType().contains("1")){
                            borrowRecordRespVO.setBorrowStatusName("借阅中");
                        }else {
                            borrowRecordRespVO.setBorrowStatusName("待取档");
                        }

                    } else if (returnTime != null && returnTime.after(date)) {
                        if(ObjectUtil.isNotEmpty(borrowRecordRespVO.getIsReturn())){
                            if(borrowRecordRespVO.getIsReturn()==0){
                                // 计算剩余时间
                                Instant returnInstant = returnTime.toInstant();
                                Instant submitInstant = date.toInstant();
                                Duration duration = Duration.between(submitInstant, returnInstant);
                                long remainTimeInSeconds = duration.getSeconds();
                                long days = remainTimeInSeconds / (24 * 3600);
                                long hours = (remainTimeInSeconds % (24 * 3600)) / 3600;
                                borrowRecordRespVO.setRemainTime(days + "天" + hours + "小时");
                                borrowRecordRespVO.setBorrowStatusName("借阅中");
                            } else if(borrowRecordRespVO.getIsReturn()==1){
                                borrowRecordRespVO.setBorrowStatusName("已归还");
                            }
                        } else {
                            // 计算剩余时间
                            Instant returnInstant = returnTime.toInstant();
                            Instant submitInstant = date.toInstant();
                            Duration duration = Duration.between(submitInstant, returnInstant);
                            long remainTimeInSeconds = duration.getSeconds();
                            long days = remainTimeInSeconds / (24 * 3600);
                            long hours = (remainTimeInSeconds % (24 * 3600)) / 3600;
                            borrowRecordRespVO.setRemainTime(days + "天" + hours + "小时");
                            borrowRecordRespVO.setBorrowStatusName("借阅中");
                        }

                    } else {
                        if(ObjectUtil.isNotEmpty(borrowRecordRespVO.getIsReturn())){
                            if(borrowRecordRespVO.getIsReturn()==1){
                                borrowRecordRespVO.setBorrowStatusName("已归还");
                            }else if(borrowRecordRespVO.getBorrowType().contains("1") && borrowRecordRespVO.getIsReturn()==0){
                                borrowRecordRespVO.setBorrowStatusName("借阅中");
                            }
                        }
                        if(borrowRecordRespVO.getBorrowType().equals("0")){
                            borrowRecordRespVO.setBorrowStatusName("已归还");
                        }

                    }
                }else {
                    borrowRecordRespVO.setBorrowStatusName(CommonFlowableReqVOResultStatusEnums.getInstance(borrowRecordRespVO.getResult()).getInfo());
                }

            }
        }
        return result;
    }

    private Result getResult() {
        List<DictDataRespDTO> archiveStorageYearList = dictDataApi.getDictDataList("storage_life");
        Map<String, DictDataRespDTO> map1 = CollectionUtils.convertMap(archiveStorageYearList, DictDataRespDTO::getValue);
        List<DictDataRespDTO> openStatusNameList = dictDataApi.getDictDataList("opening_situation");
        Map<String, DictDataRespDTO> map2 = CollectionUtils.convertMap(openStatusNameList, DictDataRespDTO::getValue);
        List<DictDataRespDTO> archiveCarrierList = dictDataApi.getDictDataList("archive_carrier");
        Map<String, DictDataRespDTO> map3 = CollectionUtils.convertMap(archiveCarrierList, DictDataRespDTO::getValue);
        return new Result(map1, map2, map3);
    }
    private static class Result {
        public final Map<String, DictDataRespDTO> map1;
        public final Map<String, DictDataRespDTO> map2;
        public final Map<String, DictDataRespDTO> map3;

        public Result(Map<String, DictDataRespDTO> map1, Map<String, DictDataRespDTO> map2, Map<String, DictDataRespDTO> map3) {
            this.map1 = map1;
            this.map2 = map2;
            this.map3 = map3;
        }
    }

}
