package com.yaoan.module.econtract.service.contract.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.convert.contract.ContractXmlConfigConvert;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractXmlConfigDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.contract.ContractXmlConfigMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.contract.ContractXmlConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class ContractXmlConfigServiceImpl implements ContractXmlConfigService {

    @Resource
    private ContractXmlConfigMapper contractXmlConfigMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;

    @Override
    public String addContractXmlInfo(ContractXmlConfigCreateVO contractXmlInfo) {
        ContractXmlConfigDO contractXmlConfigDO = ContractXmlConfigConvert.INSTANCE.createvo2DO(contractXmlInfo);
        if (ObjectUtil.isNotEmpty(contractXmlInfo.getConfigData())) {
            contractXmlConfigDO.setConfigJson(JSONObject.toJSONString(contractXmlInfo.getConfigData()));
        }
        contractXmlConfigMapper.insert(contractXmlConfigDO);
        return contractXmlConfigDO.getId();
    }

    @Override
    public void deleteContractXmlInfo(String id) {
        ContractXmlConfigDO contractXmlConfigDO = contractXmlConfigMapper.selectById(id);
        if (ObjectUtil.isEmpty(contractXmlConfigDO)) {
            throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "数据不存在");
        }
        contractXmlConfigMapper.deleteById(id);
    }

    @Override
    public String updateContractXmlInfo(ContractXmlConfigCreateVO contractXmlInfo) {
        if (ObjectUtil.isEmpty(contractXmlInfo.getId())) {
            throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "id不可为空");
        }
        ContractXmlConfigDO info = contractXmlConfigMapper.selectById(contractXmlInfo.getId());
        if (ObjectUtil.isEmpty(info)) {
            throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "数据不存在");
        }
        ContractXmlConfigDO contractXmlConfigDO = ContractXmlConfigConvert.INSTANCE.createvo2DO(contractXmlInfo);
        if (ObjectUtil.isNotEmpty(contractXmlInfo.getConfigData())) {
            contractXmlConfigDO.setConfigJson(JSONObject.toJSONString(contractXmlInfo.getConfigData()));
        }
        contractXmlConfigMapper.updateById(contractXmlConfigDO);
        return contractXmlConfigDO.getId();
    }

    @Override
    public ContractXmlConfigVO getContractXmlInfoById(String id) {
        ContractXmlConfigDO info = contractXmlConfigMapper.selectById(id);
        if (ObjectUtil.isEmpty(info)) {
            throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "数据不存在");
        }
        ContractXmlConfigVO contractXmlConfigVO = ContractXmlConfigConvert.INSTANCE.do2VO(info);
        if(StringUtils.isNotEmpty(info.getConfigJson())){
            contractXmlConfigVO.setConfigData(JSONObject.parseObject(info.getConfigJson(), ConfigDataVO.class));
        }
        return contractXmlConfigVO;
    }

    @Override
    public PageResult<ContractXmlConfigPageVO> getAllContractXmlInfos(ContractXmlConfigQueryVO contractXmlInfoDO) {
        MPJLambdaWrapper<ContractXmlConfigDO> objectMPJLambdaWrapper = new MPJLambdaWrapper<>();
        if (StringUtils.isNotEmpty(contractXmlInfoDO.getContractType())) {
            objectMPJLambdaWrapper.eq(ContractXmlConfigDO::getContractType, contractXmlInfoDO.getContractType());
        }
        if (StringUtils.isNotEmpty(contractXmlInfoDO.getCode())) {
            objectMPJLambdaWrapper.like(ContractXmlConfigDO::getCode, contractXmlInfoDO.getCode());
        }
        if (StringUtils.isNotEmpty(contractXmlInfoDO.getName())) {
            objectMPJLambdaWrapper.like(ContractXmlConfigDO::getName, contractXmlInfoDO.getName());
        }
        if (ObjectUtil.isNotEmpty(contractXmlInfoDO.getStatus())) {
            objectMPJLambdaWrapper.eq(ContractXmlConfigDO::getStatus, contractXmlInfoDO.getStatus());
        }
        if(ObjectUtil.isNotEmpty(contractXmlInfoDO.getStartCreateTime()) && ObjectUtil.isNotEmpty(contractXmlInfoDO.getEndCreateTime())){
            objectMPJLambdaWrapper.between(ContractXmlConfigDO::getCreateTime, contractXmlInfoDO.getStartCreateTime(), contractXmlInfoDO.getEndCreateTime());
        }
        PageResult<ContractXmlConfigDO> result = contractXmlConfigMapper.selectPage(contractXmlInfoDO, objectMPJLambdaWrapper);
        PageResult<ContractXmlConfigPageVO> pageResult = ContractXmlConfigConvert.INSTANCE.convertPage(result);
        if(pageResult.getTotal()>0){
            List<String> typeId = pageResult.getList().stream().map(ContractXmlConfigPageVO::getContractType).collect(Collectors.toList());
            List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().in(ContractType::getId, typeId));
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypes, ContractType::getId);
            pageResult.getList().forEach(item -> {
                ContractType type = contractTypeMap.get(item.getContractType());
                if(ObjectUtil.isNotEmpty(type)){
                    item.setContractTypeName(type.getName());
                }
            });
        }
        return pageResult;
    }
}
