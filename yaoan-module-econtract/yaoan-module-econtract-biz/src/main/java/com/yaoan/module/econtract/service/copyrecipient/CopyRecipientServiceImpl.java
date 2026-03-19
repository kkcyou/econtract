package com.yaoan.module.econtract.service.copyrecipient;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.copyrecipient.vo.CopyRecipientPageReqVO;
import com.yaoan.module.econtract.controller.admin.copyrecipient.vo.CopyRecipientPageRespVO;
import com.yaoan.module.econtract.convert.copyrecipient.CopyRecipientConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.bpm.model.ModelBpmDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.copyrecipients.BpmCopyRecipientsDO;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.model.ModelBpmMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.copyrecipients.BpmCopyRecipientsMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/1 22:37
 */
@Service
public class CopyRecipientServiceImpl implements CopyRecipientService {

    @Resource
    private BpmCopyRecipientsMapper bpmCopyRecipientsMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private BpmContractMapper bpmContractMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private ModelBpmMapper modelBpmMapper;

    @Override
    public PageResult<CopyRecipientPageRespVO> list(CopyRecipientPageReqVO reqVO) {
        PageResult<BpmCopyRecipientsDO> doPage = bpmCopyRecipientsMapper.selectPage(reqVO);
        PageResult<CopyRecipientPageRespVO> result = CopyRecipientConverter.INSTANCE.pageDO2Resp(doPage);

        return enhancePage(result, reqVO.getProcessDefinitionKey());
    }

    private PageResult<CopyRecipientPageRespVO> enhancePage(PageResult<CopyRecipientPageRespVO> result, String processDefinitionKey) {
        List<CopyRecipientPageRespVO> respVOList = result.getList();
        if (CollectionUtil.isEmpty(respVOList)) {
            return new PageResult<CopyRecipientPageRespVO>().setTotal(result.getTotal()).setList(Collections.emptyList());
        }
        ActivityConfigurationEnum activityConfigurationEnum = ActivityConfigurationEnum.getInstanceByDefKey(processDefinitionKey);
        if (ObjectUtil.isNull(activityConfigurationEnum)) {
            return new PageResult<CopyRecipientPageRespVO>().setTotal(result.getTotal()).setList(Collections.emptyList());
        }
        Map<String, Map<String, String>> entityMap = new HashMap<String, Map<String, String>>();
        Map<String, Map<String, String>> bpmMap = new HashMap<String, Map<String, String>>();
        entityMap = getEntityMap(respVOList, activityConfigurationEnum);
        bpmMap = getBpmMap(respVOList, activityConfigurationEnum);

        List<AdminUserRespDTO> userList = adminUserApi.getUserList();
        Map<Long, AdminUserRespDTO> userMap = new HashMap<Long, AdminUserRespDTO>();
        if (CollectionUtil.isNotEmpty(userList)) {
            userMap = CollectionUtils.convertMap(userList, AdminUserRespDTO::getId);
        }

        for (CopyRecipientPageRespVO respVO : result.getList()) {
            //审批人
            AdminUserRespDTO userDTO = userMap.get(Long.valueOf(respVO.getCreator()));
            if (ObjectUtil.isNotNull(userDTO)) {
                respVO.setApprover(userDTO.getNickname());
            }

            //业务相关数据
            Map<String, String> respEntityMap = entityMap.get(respVO.getId());
            if (CollectionUtil.isNotEmpty(respEntityMap)) {
                respVO.setName(respEntityMap.get("name"));
                respVO.setId(respEntityMap.get("id"));
            }


            //流程相关数据
            Map<String, String> respBpmMap = entityMap.get(respVO.getId());
            if (CollectionUtil.isNotEmpty(respBpmMap)) {
                //流程实例
                respVO.setProcessInstanceId(respBpmMap.get("processInstanceId"));

                //申请人
                Long creator = Long.valueOf(respBpmMap.get("creator"));
                AdminUserRespDTO userRespDTO = userMap.get(creator);
                if (ObjectUtil.isNotNull(userDTO)) {
                    respVO.setSubmitter(userRespDTO.getNickname());
                }
            }

        }
        return result;
    }


    /**
     * 获得业务信息
     */

    private Map<String, Map<String, String>> getEntityMap(List<CopyRecipientPageRespVO> respVOList, ActivityConfigurationEnum activityConfigurationEnum) {
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
        List<String> doIdList = respVOList.stream().map(CopyRecipientPageRespVO::getBusinessId).collect(Collectors.toList());
        switch (activityConfigurationEnum) {
            case MODEL_APPROVE: {
                List<Model> doList = modelMapper.selectList(new LambdaQueryWrapperX<Model>().inIfPresent(Model::getId, doIdList));
                if (CollectionUtil.isNotEmpty(doList)) {
                    for (Model entity : doList) {
                        Map<String, String> tempMap = new HashMap<String, String>();
                        tempMap.put("id", entity.getId());
                        tempMap.put("code", entity.getCode());
                        tempMap.put("name", entity.getName());
                        result.put(entity.getId(), tempMap);
                    }
                }
                return result;
            }

            case CONTRACT_DRAFT_APPROVE: {
                List<ContractDO> doList = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().inIfPresent(ContractDO::getId, doIdList));
                if (CollectionUtil.isNotEmpty(doList)) {
                    for (ContractDO entity : doList) {
                        Map<String, String> tempMap = new HashMap<String, String>();
                        tempMap.put("id", entity.getId());
                        tempMap.put("code", entity.getCode());
                        tempMap.put("name", entity.getName());

                        result.put(entity.getId(), tempMap);
                    }
                }
                return result;
            }
            default:
                return result;
        }
    }

    /**
     * 获得流程信息
     */
    private Map<String, Map<String, String>> getBpmMap(List<CopyRecipientPageRespVO> respVOList, ActivityConfigurationEnum activityConfigurationEnum) {
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
        List<String> doIdList = respVOList.stream().map(CopyRecipientPageRespVO::getBusinessId).collect(Collectors.toList());
        switch (activityConfigurationEnum) {

            case MODEL_APPROVE: {
                List<ModelBpmDO> bpmList = modelBpmMapper.selectList(new LambdaQueryWrapperX<ModelBpmDO>().inIfPresent(ModelBpmDO::getModelId, doIdList));
                if (CollectionUtil.isNotEmpty(bpmList)) {
                    for (ModelBpmDO entity : bpmList) {
                        Map<String, String> tempMap = new HashMap<String, String>();
                        tempMap.put("id", entity.getId());
                        tempMap.put("businessId", entity.getModelId());
                        tempMap.put("processInstanceId", entity.getProcessInstanceId());
                        tempMap.put("creator", entity.getCreator());
                        tempMap.put("createTime", String.valueOf(entity.getCreateTime()));
                        tempMap.put("result", String.valueOf(entity.getResult()));

                        result.put(entity.getId(), tempMap);
                    }
                }
                return result;
            }

            case CONTRACT_DRAFT_APPROVE: {
                List<BpmContract> bpmList = bpmContractMapper.selectList(new LambdaQueryWrapperX<BpmContract>().inIfPresent(BpmContract::getContractId, doIdList));
                if (CollectionUtil.isNotEmpty(bpmList)) {
                    for (BpmContract entity : bpmList) {
                        Map<String, String> tempMap = new HashMap<String, String>();
                        tempMap.put("id", entity.getId());
                        tempMap.put("businessId", entity.getContractId());
                        tempMap.put("processInstanceId", entity.getProcessInstanceId());
                        tempMap.put("creator", entity.getCreator());
                        tempMap.put("createTime", String.valueOf(entity.getCreateTime()));
                        tempMap.put("result", String.valueOf(entity.getResult()));

                        result.put(entity.getId(), tempMap);
                    }
                }
                return result;
            }
            default:
                return result;
        }
    }

}
