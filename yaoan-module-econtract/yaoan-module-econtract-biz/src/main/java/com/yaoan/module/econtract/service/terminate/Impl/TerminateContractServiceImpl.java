package com.yaoan.module.econtract.service.terminate.Impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.econtract.controller.admin.terminate.vo.TerminateContractCreateReqVO;
import com.yaoan.module.econtract.convert.terminate.TerminateContractConvert;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.terminate.TerminateContractDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.terminate.TerminateContractMapper;
import com.yaoan.module.econtract.enums.ContractPerfEnums;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.PerfTaskEnums;
import com.yaoan.module.econtract.service.terminate.TerminateContractService;
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
public class TerminateContractServiceImpl implements TerminateContractService {
    @Resource
    private TerminateContractMapper terminateContractMapper;
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
     * 添加终止合同(修改)
     *
     * @param terminateContractCreateReqVO
     * @return
     */
    @Override
    public String create(TerminateContractCreateReqVO terminateContractCreateReqVO) throws Exception {

        TerminateContractDO terminateContractDO = TerminateContractConvert.INSTANCE.toEntity(terminateContractCreateReqVO);
        //主合同不存在报异常
        ContractDO contract = contractMapper.selectById(terminateContractCreateReqVO.getContractId());
        if (contract == null) {
            throw exception(NO_DATA_FIND_ERROR);
        }

        //添加（修改）文件名称
//        terminateContractDO.setFileName(terminateContractCreateReqVO.getFile().getOriginalFilename());
//        //上传签署文件到minio
//        MultipartFile file = terminateContractCreateReqVO.getFile();
//        String source = "terminateContract" + File.separator + DateUtil.today() + File.separator + System.currentTimeMillis() + "-" + file.getOriginalFilename();
//        Long id = fileApi.uploadFile(file.getOriginalFilename(), source, IoUtil.readBytes(file.getInputStream()));
//        terminateContractDO.setFileAddId(id);
        if (ObjectUtil.isNotEmpty(terminateContractCreateReqVO.getId())) {
            fileApi.deleteFile(terminateContractCreateReqVO.getFileAddId());
            terminateContractMapper.updateById(terminateContractDO);
        } else {
            terminateContractMapper.insert(terminateContractDO);

            //发起工作流
            //流程变量
            Map<String, Object> processInstanceVariables = new HashMap<>();
            processInstanceVariables.put("assign0", SecurityFrameworkUtils.getLoginUserId());
            //获取签署方用户id集合
            List<Long> signatoryIdList = new ArrayList();
            //获取发起方用户id
            String creator = contractMapper.selectById(terminateContractDO.getContractId()).getCreator();
            signatoryIdList.add(Long.valueOf(creator));
            //获取签署方中相对方的用户id集合
            List<String> idList = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, terminateContractDO.getContractId())
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
                String processInstance = processInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(), new BpmProcessInstanceCreateReqDTO().setVariables(processInstanceVariables).setBusinessKey(terminateContractDO.getId()).setProcessDefinitionKey(PROCESS_KEY_BOTH));
                terminateContractDO.setProcessInstanceId(processInstance);
            }
            if (processInstanceVariables.size()==3){
                String processInstance = processInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(), new BpmProcessInstanceCreateReqDTO().setVariables(processInstanceVariables).setBusinessKey(terminateContractDO.getId()).setProcessDefinitionKey(PROCESS_KEY_TRIPARTITE));
                terminateContractDO.setProcessInstanceId(processInstance);
            }
            terminateContractMapper.updateById(terminateContractDO);
        }
        return terminateContractDO.getId();
    }

    /**
     * 添加终止合同(修改)
     *
     * @param terminateContractCreateReqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createV2(TerminateContractCreateReqVO terminateContractCreateReqVO) throws Exception {
        TerminateContractDO terminateContractDO = TerminateContractConvert.INSTANCE.toEntity(terminateContractCreateReqVO);
        //主合同不存在报异常
        ContractDO contract = contractMapper.selectById(terminateContractCreateReqVO.getContractId());
        if (contract == null) {
            throw exception(NO_DATA_FIND_ERROR);
        }
        //修改
        if (ObjectUtil.isNotEmpty(terminateContractCreateReqVO.getId())) {
            fileApi.deleteFile(terminateContractCreateReqVO.getFileAddId());
            terminateContractMapper.updateById(terminateContractDO);
        } else {
            //新增
            terminateContractMapper.insert(terminateContractDO);
            //修改合同状态、履约状态、履约任务状态
            ContractDO contractDO = new ContractDO();
            contractDO.setId(terminateContractDO.getContractId());
            contractDO.setStatus(ContractStatusEnums.TERMINATED.getCode());
            contractMapper.updateById(contractDO);
            ContractPerformanceDO contractPerformanceDO = contractPerforMapper.selectOne(ContractPerformanceDO::getContractId, terminateContractDO.getContractId());
            if (ObjectUtil.isNotEmpty(contractPerformanceDO)){
                contractPerformanceDO.setContractStatus(ContractPerfEnums.TERMINATED.getCode());
                contractPerforMapper.updateById(contractPerformanceDO);
                List<PerfTaskDO> perfTaskDOList = perforTaskMapper.selectList(PerfTaskDO::getContractPerfId, contractPerformanceDO.getId());
                if (CollectionUtil.isNotEmpty(perfTaskDOList)){
                    for (PerfTaskDO perfTaskDO : perfTaskDOList) {
                        if (PerfTaskEnums.IN_PERFORMANCE.getCode().equals(perfTaskDO.getTaskStatus())){
                            perfTaskDO.setTaskStatus(PerfTaskEnums.PERFORMANCE_END.getCode());
                        }
                        if (PerfTaskEnums.PERFORMANCE_OVER_TIME.getCode().equals(perfTaskDO.getTaskStatus())){
                            perfTaskDO.setTaskStatus(PerfTaskEnums.OVER_TIME_END.getCode());
                        }
                        perforTaskMapper.updateById(perfTaskDO);
                    }
                }
            }
        }
        return terminateContractDO.getId();
    }
}