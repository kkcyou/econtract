package com.yaoan.module.econtract.service.freezed.Impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.econtract.controller.admin.freezed.vo.FreezedContractCreateReqVO;
import com.yaoan.module.econtract.convert.freezed.FreezedContractConvert;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.freezed.FreezedContractDO;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.freezed.FreezedContractMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.enums.ContractPerfEnums;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.PerfTaskEnums;
import com.yaoan.module.econtract.service.freezed.FreezedContractService;
import com.yaoan.module.infra.api.file.FileApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.NO_DATA_FIND_ERROR;

@Service
@Slf4j
public class FreezedContractServiceImpl implements FreezedContractService {
    @Resource
    private FreezedContractMapper freezedContractMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private ContractMapper contractMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Resource
    private SignatoryRelMapper signatoryRelMapper;

    @Resource
    private RelativeMapper relativeMapper;

    @Resource
    private ContractPerforMapper contractPerforMapper;

    @Resource
    private PerforTaskMapper perforTaskMapper;

    public static final String PROCESS_KEY_BOTH = "ecms_contract_terminate_both";

    public static final String PROCESS_KEY_TRIPARTITE = "ecms_contract_sign_tripartite";
    /**
     * 合同冻结
     *
     * @param freezedContractCreateReqVO
     * @return
     */
    @Override
    public String create(FreezedContractCreateReqVO freezedContractCreateReqVO) throws Exception {

        FreezedContractDO freezedContractDO = FreezedContractConvert.INSTANCE.toEntity(freezedContractCreateReqVO);
        //主合同不存在报异常
        ContractDO contract = contractMapper.selectById(freezedContractCreateReqVO.getContractId());
        if (contract == null) {
            throw exception(NO_DATA_FIND_ERROR);
        }

        if (ObjectUtil.isNotEmpty(freezedContractCreateReqVO.getId())) {
            fileApi.deleteFile(freezedContractCreateReqVO.getFileAddId());
            freezedContractMapper.updateById(freezedContractDO);
        } else {
            freezedContractMapper.insert(freezedContractDO);

            //发起工作流
            //流程变量
            Map<String, Object> processInstanceVariables = new HashMap<>();
            processInstanceVariables.put("assign0", SecurityFrameworkUtils.getLoginUserId());
            //获取签署方用户id集合
            List<Long> signatoryIdList = new ArrayList();
            //获取发起方用户id
            String creator = contractMapper.selectById(freezedContractDO.getContractId()).getCreator();
            signatoryIdList.add(Long.valueOf(creator));
            //获取签署方中相对方的用户id集合
            List<String> idList = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, freezedContractDO.getContractId())
                    .stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
            List<Long> userIdList = relativeMapper.selectBatchIds(idList).stream().map(Relative::getContactId).collect(Collectors.toList());
            signatoryIdList.addAll(userIdList);
            //签署方用户id集合除去登录用户-流程变量0
            signatoryIdList.remove(SecurityFrameworkUtils.getLoginUserId());
            //流程变量顺序
            int i = 1;
            for (Long assign : signatoryIdList) {
                processInstanceVariables.put("assign"+i,assign);
                i++;
            }
            if (processInstanceVariables.size()==2){
                String processInstance = processInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(), new BpmProcessInstanceCreateReqDTO().setVariables(processInstanceVariables).setBusinessKey(freezedContractDO.getId()).setProcessDefinitionKey(PROCESS_KEY_BOTH));
                freezedContractDO.setProcessInstanceId(processInstance);
            }
            if (processInstanceVariables.size()==3){
                String processInstance = processInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(), new BpmProcessInstanceCreateReqDTO().setVariables(processInstanceVariables).setBusinessKey(freezedContractDO.getId()).setProcessDefinitionKey(PROCESS_KEY_TRIPARTITE));
                freezedContractDO.setProcessInstanceId(processInstance);
            }
            freezedContractMapper.updateById(freezedContractDO);
        }
        return freezedContractDO.getId();
    }

    /**
     * 合同冻结
     *
     * @param freezedContractCreateReqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createV2(FreezedContractCreateReqVO freezedContractCreateReqVO) throws Exception {
        FreezedContractDO freezedContractDO = FreezedContractConvert.INSTANCE.toEntity(freezedContractCreateReqVO);
        //设置申请类型未冻结 0 
        freezedContractDO.setType(0);
        //主合同不存在报异常
        ContractDO contract = contractMapper.selectById(freezedContractCreateReqVO.getContractId());
        if (contract == null) {
            throw exception(NO_DATA_FIND_ERROR);
        }
        //修改
        if (ObjectUtil.isNotEmpty(freezedContractCreateReqVO.getId())) {
            fileApi.deleteFile(freezedContractCreateReqVO.getFileAddId());
            freezedContractMapper.updateById(freezedContractDO);
        } else {
            //新增
            freezedContractMapper.insert(freezedContractDO);
            //修改合同状态、履约状态、履约任务状态
            ContractDO contractDO = new ContractDO();
            contractDO.setId(freezedContractDO.getContractId());
            contractDO.setStatus(ContractStatusEnums.FREEZED.getCode());
            contractMapper.updateById(contractDO);
            ContractPerformanceDO contractPerformanceDO = contractPerforMapper.selectOne(ContractPerformanceDO::getContractId, freezedContractDO.getContractId());
            if (ObjectUtil.isNotEmpty(contractPerformanceDO)){
                contractPerformanceDO.setContractStatus(ContractPerfEnums.FREEZED.getCode());
                contractPerforMapper.updateById(contractPerformanceDO);
                List<PerfTaskDO> perfTaskDOList = perforTaskMapper.selectList(PerfTaskDO::getContractPerfId, contractPerformanceDO.getId());
                if (CollectionUtil.isNotEmpty(perfTaskDOList)){
                    for (PerfTaskDO perfTaskDO : perfTaskDOList) {
                        if (perfTaskDO.getTaskStatus() == PerfTaskEnums.IN_PERFORMANCE.getCode()){
                            perfTaskDO.setTaskStatus(PerfTaskEnums.PERFORMANCE_PAUSE.getCode());
                        }
                        if (perfTaskDO.getTaskStatus() == PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode()){
                            perfTaskDO.setTaskStatus(PerfTaskEnums.OVER_TIME_PAUSE.getCode());
                        }
                        perforTaskMapper.updateById(perfTaskDO);
                    }
                }
            }
        }
        return freezedContractDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String unFreezed(FreezedContractCreateReqVO freezedContractCreateReqVO) throws Exception {
        FreezedContractDO freezedContractDO = FreezedContractConvert.INSTANCE.toEntity(freezedContractCreateReqVO);
        //设置申请类型未解冻 1
        freezedContractDO.setType(1);
        //主合同不存在报异常
        ContractDO contract = contractMapper.selectById(freezedContractCreateReqVO.getContractId());
        if (contract == null) {
            throw exception(NO_DATA_FIND_ERROR);
        }
        //修改
        if (ObjectUtil.isNotEmpty(freezedContractCreateReqVO.getId())) {
            fileApi.deleteFile(freezedContractCreateReqVO.getFileAddId());
            freezedContractMapper.updateById(freezedContractDO);
        } else {
            //新增
            freezedContractMapper.insert(freezedContractDO);
            //修改合同状态、履约状态、履约任务状态
            ContractDO contractDO = new ContractDO();
            contractDO.setId(freezedContractDO.getContractId());
            contractDO.setStatus(ContractStatusEnums.SIGN_COMPLETED.getCode());
            contractMapper.updateById(contractDO);
            ContractPerformanceDO contractPerformanceDO = contractPerforMapper.selectOne(ContractPerformanceDO::getContractId, freezedContractDO.getContractId());
            if (ObjectUtil.isNotEmpty(contractPerformanceDO)){
                contractPerformanceDO.setContractStatus(ContractPerfEnums.IN_PERFORMANCE.getCode());
                contractPerforMapper.updateById(contractPerformanceDO);
                List<PerfTaskDO> perfTaskDOList = perforTaskMapper.selectList(PerfTaskDO::getContractPerfId, contractPerformanceDO.getId());
                if (CollectionUtil.isNotEmpty(perfTaskDOList)){
                    for (PerfTaskDO perfTaskDO : perfTaskDOList) {
                        if (perfTaskDO.getTaskStatus() == PerfTaskEnums.PERFORMANCE_PAUSE.getCode()){
                            perfTaskDO.setTaskStatus(PerfTaskEnums.IN_PERFORMANCE.getCode());
                        }
                        if (perfTaskDO.getTaskStatus() == PerfTaskEnums.OVER_TIME_PAUSE.getCode()){
                            perfTaskDO.setTaskStatus(PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode());
                        }
                        perforTaskMapper.updateById(perfTaskDO);
                    }
                }
            }
        }
        return freezedContractDO.getId();
    }
}