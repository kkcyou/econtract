package com.yaoan.module.econtract.service.alert;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.query.MPJQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.alert.vo.AlertReqVO;
import com.yaoan.module.econtract.controller.admin.alert.vo.AlertRespVO;
import com.yaoan.module.econtract.convert.alert.AlertConverter;
import com.yaoan.module.econtract.dal.dataobject.alert.AlertDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import com.yaoan.module.econtract.dal.mysql.alert.AlertMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.SimpleContractMapper;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.StatusConstants;
import com.yaoan.module.econtract.enums.process.ContractProcessStageEnums;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.yaoan.module.econtract.enums.StatusConstants.*;

/**
 * @description:
 * @author: Pele
 * @date: 2023/11/8 15:55
 */
@Service
public class AlertServiceImpl implements AlertService {

    static final Long DEFAULT_TENANT_ID = 1L;
    @Resource
    private AlertMapper alertMapper;
    @Resource
    private SimpleContractMapper simpleContractMapper;
    @Resource
    private BpmContractMapper bpmContractMapper;

    @Override
    public void deleteAllAlertsByProcessInstanceId(String processInstanceId) {
        alertMapper.delete(new LambdaQueryWrapperX<AlertDO>().eq(AlertDO::getProcessInstanceId, processInstanceId));
    }

    @Override
    public void addAlertByProcessInstanceId(String processInstanceId, String taskDefinitionKey) {
        //找到关联的合同
        MPJQueryWrapper<SimpleContractDO> queryWrapper = new MPJQueryWrapper<SimpleContractDO>().selectAll(SimpleContractDO.class)
                .leftJoin("ecms_bpm_contract s on s.contract_id = t.id");
        // TODO 存在sql注入风险
        queryWrapper.inSql("t.id", " SELECT s.contract_id FROM ecms_bpm_contract s WHERE s.deleted = 0 AND s.process_instance_id = " + "'" + processInstanceId + "'");
        //赋值插入记录
        AlertDO alertDO = (AlertDO) new AlertDO().setProcessInstanceId(processInstanceId)
                .setFlowStage(taskDefinitionKey).setTenantId(DEFAULT_TENANT_ID);
        if (ObjectUtil.isNotNull(alertDO)) {
            alertMapper.insert(alertDO);
        }
    }

    @Override
    public PageResult<AlertRespVO> getContractAlertPage(AlertReqVO vo) {
        PageResult<AlertDO> pageResult = alertMapper.selectPage(vo, new LambdaQueryWrapperX<AlertDO>().orderByDesc(AlertDO::getCreateTime));
        PageResult<AlertRespVO> respVOPageResult = AlertConverter.INSTANCE.DOList2VOList(pageResult);
        List<AlertDO> alertDOList = pageResult.getList();
        if (CollectionUtil.isNotEmpty(alertDOList)) {
            List<AlertRespVO> respVOS = AlertConverter.INSTANCE.convertDO2RespVO(alertDOList);
            List<SimpleContractDO> contractDOList = simpleContractMapper.selectList();
            Map<String, SimpleContractDO> contractDOMap = CollectionUtils.convertMap(contractDOList, SimpleContractDO::getId);
            List<BpmContract> bpmContractList = bpmContractMapper.selectList();
            Map<String, BpmContract> bpmContractMap = CollectionUtils.convertMap(bpmContractList, BpmContract::getProcessInstanceId);
            for (AlertRespVO respVO : respVOS) {
                //虚拟系统的新推送，没有流程实例id
                if (StringUtils.isNotBlank(respVO.getProcessInstanceId())) {
                    BpmContract bpmContract = bpmContractMap.get(respVO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(bpmContract)) {
                        SimpleContractDO simpleContractDO = contractDOMap.get(bpmContract.getContractId());
                        if (ObjectUtil.isNotNull(simpleContractDO)) {
                            respVO.setContractId(simpleContractDO.getId());
                            respVO.setContractName(simpleContractDO.getName());
                            respVO.setAlertContent(getContent(respVO, simpleContractDO));
                        }
                    }
                }else {
                    respVO.setAlertContent(getContent(respVO, null));
                }
            }
            return respVOPageResult.setList(respVOS);
        }
        return new PageResult<AlertRespVO>();
    }

    /**
     * 分别赋值流程信息
     */
    private String getContent(AlertRespVO respVO, SimpleContractDO simpleContractDO) {
        ////待草拟(三方系统) 新推送的就没有相关合同信息
        if(ObjectUtil.isNull(simpleContractDO)){
            return buildAlertResult(respVO.getBusinessName(), WAITE_TO_BE_CREATE_CONTRACT);
        }

        String contractName = simpleContractDO.getName();
        String taskDefinitionKey = respVO.getFlowStage();
        ContractStatusEnums contractStatusEnums = ContractStatusEnums.getInstance(simpleContractDO.getStatus());
        if (ObjectUtil.isNotNull(contractStatusEnums)) {
            //确认阶段 待处理
            if (StringUtils.contains(taskDefinitionKey, ContractProcessStageEnums.CONTRACT_FLOW_STAGE_CONFIRM.getCode())) {
                return buildAlertResult(contractName, WAITE_TO_BE_HANDLE);
            }
            //签署阶段 待处理
            if (StringUtils.contains(taskDefinitionKey, ContractProcessStageEnums.CONTRACT_FLOW_STAGE_SIGN.getCode())) {
                return buildAlertResult(contractName, WAITE_TO_BE_HANDLE);
            }
            //审批阶段
            if (StringUtils.contains(taskDefinitionKey, ContractProcessStageEnums.CONTRACT_FLOW_STAGE_APPROVE_LAW_WORKS.getCode()) || StringUtils.contains(taskDefinitionKey,
                    ContractProcessStageEnums.CONTRACT_FLOW_STAGE_APPROVE_DEPARTMENT_LEADER.getCode())) {
                if (ContractStatusEnums.CHECKING == contractStatusEnums) {
                    //待审批
                    return buildAlertResult(contractName, WAITE_TO_BE_APPROVED);
                } else {
                    //通过 或 退回
                    return buildAlertResult(contractName, contractStatusEnums.getDesc());
                }
            }



        }
        return "";
    }

    private String buildAlertResult(String contractName, String solution) {
        if (StringUtils.isNotBlank(solution)) {
            return contractName + " " + solution + StatusConstants.PLEASE_PAY_ATTENTION;
        }
        return contractName + " " + WAITE_TO_BE_APPROVED + StatusConstants.PLEASE_PAY_ATTENTION;
    }

}

