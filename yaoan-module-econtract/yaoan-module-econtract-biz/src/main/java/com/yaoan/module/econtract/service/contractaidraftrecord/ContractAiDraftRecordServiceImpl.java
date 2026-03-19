package com.yaoan.module.econtract.service.contractaidraftrecord;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yaoan.framework.common.exception.util.ServiceExceptionUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.AITemplateInfo;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.ContractAiDraftRecordCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.ContractAiDraftRecordPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.ContractAiDraftRecordUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.contracttype.vo.ContractTypeSelectReqVO;
import com.yaoan.module.econtract.convert.contractaidraftrecord.ContractAiDraftRecordConvert;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractaidraftrecord.ContractAiDraftRecordDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contractaidraftrecord.ContractAiDraftRecordMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 合同智能起草记录 Service 实现类
 *
 * @author doujiale
 */
@Slf4j
@Service
@Validated
public class ContractAiDraftRecordServiceImpl implements ContractAiDraftRecordService {

    @Resource
    private ContractAiDraftRecordMapper contractAiDraftRecordMapper;

    @Resource
    private ContractTypeMapper contractTypeMapper;

    @Resource
    private ContractMapper contractMapper;

    @Resource
    private ModelMapper modelMapper;

    @Override
    public Long createContractAiDraftRecord(ContractAiDraftRecordCreateReqVO createReqVO) {

        if (StringUtils.isEmpty(createReqVO.getTitle())) {
            createReqVO.setTitle(createReqVO.getContractName());
        }

        // 插入
        ContractAiDraftRecordDO contractAiDraftRecord = ContractAiDraftRecordConvert.INSTANCE.convert(createReqVO);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mainBody", createReqVO.getMainBody());
        jsonObject.put("target", createReqVO.getTarget());
        jsonObject.put("priceAndPayment", createReqVO.getPriceAndPayment());
        jsonObject.put("transaction", createReqVO.getTransaction());

        contractAiDraftRecord.setSnapshot(jsonObject.toJSONString());
        contractAiDraftRecordMapper.insert(contractAiDraftRecord);
        // 返回
        return contractAiDraftRecord.getId();
    }

    @Override
    public void updateContractAiDraftRecord(ContractAiDraftRecordUpdateReqVO updateReqVO) {

        log.info("updateContractAiDraftRecord{}", JSONObject.toJSONString(updateReqVO));

        // 校验存在
        validateContractAiDraftRecordExists(updateReqVO.getId());

        ContractAiDraftRecordDO contractAiDraftRecordDO = contractAiDraftRecordMapper.selectById(updateReqVO.getId());

        //快照
        if (StringUtils.isNotEmpty(updateReqVO.getSnapshot())) {
            contractAiDraftRecordDO.setSnapshot(updateReqVO.getSnapshot());
        }

        //快照状态
        if (StringUtils.isNotEmpty(updateReqVO.getSnapshotStatus())) {
            contractAiDraftRecordDO.setSnapshotStatus(updateReqVO.getSnapshotStatus());
        }

        //模板信息
        if (CollectionUtil.isNotEmpty(updateReqVO.getTemplateInfoUse())) {
            contractAiDraftRecordDO.setTemplateInfo(JSONObject.toJSONString(updateReqVO.getTemplateInfoUse()));
        }

        //其他模板先信息
        if (CollectionUtil.isNotEmpty(updateReqVO.getTemplateInfoShowList())) {
            contractAiDraftRecordDO.setTemplateInfoShow(JSONObject.toJSONString(updateReqVO.getTemplateInfoShowList()));
        }

        //合同id
        if (StringUtils.isNotEmpty(updateReqVO.getContractId())) {
            contractAiDraftRecordDO.setContractId(updateReqVO.getContractId());
        }

        //合同内容
        if (StringUtils.isNotEmpty(updateReqVO.getContractContent())) {
            contractAiDraftRecordDO.setContractContent(updateReqVO.getContractContent());
        }
        //合同生成方式
        if (StringUtils.isNotEmpty(updateReqVO.getContractGenerateType())) {
            contractAiDraftRecordDO.setContractGenerateType(updateReqVO.getContractGenerateType());
        }

        //选择的模板
        if (StringUtils.isNotEmpty(updateReqVO.getTemplateSelect())) {
            contractAiDraftRecordDO.setTemplateSelect(updateReqVO.getTemplateSelect());
        }

        //使用的大模型
        if (StringUtils.isNotEmpty(updateReqVO.getLlmName())) {
            contractAiDraftRecordDO.setLlmName(updateReqVO.getLlmName());
        }


        //快照
        if (StringUtils.isNotEmpty(updateReqVO.getMainBody()) || StringUtils.isNotEmpty(updateReqVO.getTarget()) || StringUtils.isNotEmpty(updateReqVO.getPriceAndPayment()) || StringUtils.isNotEmpty(updateReqVO.getTransaction())) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mainBody", updateReqVO.getMainBody());
            jsonObject.put("target", updateReqVO.getTarget());
            jsonObject.put("priceAndPayment", updateReqVO.getPriceAndPayment());
            jsonObject.put("transaction", updateReqVO.getTransaction());

            contractAiDraftRecordDO.setSnapshot(jsonObject.toJSONString());
        }


        contractAiDraftRecordDO.setUpdateTime(LocalDateTime.now());
        // 更新
        contractAiDraftRecordMapper.updateById(contractAiDraftRecordDO);
    }

    @Override
    public void deleteContractAiDraftRecord(Long id) {
        // 校验存在
        validateContractAiDraftRecordExists(id);
        // 删除
        contractAiDraftRecordMapper.deleteById(id);
    }

    private void validateContractAiDraftRecordExists(Long id) {
        if (contractAiDraftRecordMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.invalidParamException("合同数据不存在！");
        }
    }

    @Override
    public ContractAiDraftRecordDO getContractAiDraftRecord(Long id) {
        return contractAiDraftRecordMapper.selectById(id);
    }

    @Override
    public List<ContractAiDraftRecordDO> getContractAiDraftRecordList(Collection<Long> ids) {
        return contractAiDraftRecordMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ContractAiDraftRecordDO> getContractAiDraftRecordPage(ContractAiDraftRecordPageReqVO pageReqVO) {
        return contractAiDraftRecordMapper.selectPage(pageReqVO);
    }


    @Override
    public List<ContractType> selectContractTypeList(ContractTypeSelectReqVO vo) {
        if (ObjectUtil.isNotEmpty(vo.getFlag())) {
            if (vo.getFlag() == 0) {
                return contractTypeMapper.selectList();
            }
            if (vo.getFlag() == 1) {
                LambdaQueryWrapperX<ContractType> wrapperX = new LambdaQueryWrapperX<>();
                wrapperX.select(ContractType::getId, ContractType::getParentId, ContractType::getName, ContractType::getTypePrefix
                        , ContractType::getTypeStatus, ContractType::getCode, ContractType::getCodeRuleId, ContractType::getPlatId);
                //默认查政采的合同类型
                if (ObjectUtil.isNotNull(vo.getIsGov())) {
                    switch (vo.getIsGov()) {
                        case 0:
                            wrapperX.isNull(ContractType::getPlatId);
                            break;
                        case 1:
                            wrapperX.isNotNull(ContractType::getPlatId);
                    }
                }

                //转换成树状
                return contractTypeMapper.selectList(wrapperX);
            }
        }
        return contractTypeMapper.selectList();
    }

    /**
     * 首先查询模板表，如果模板表中的数据小于3 则从模板表中查询
     * 如果大于3则从合同表中查询使用频率高的模板
     * <p>
     * 如果使用过的模板小于三个，则后面拼充模板
     *
     * @param id
     * @return
     */
    @Override
    public List<AITemplateInfo> selectModelListByContractTypeId(String id) {

        //查询模板表，查询当前合同类型的模板数量
        LambdaQueryWrapperX<Model> queryWrapper1 = new LambdaQueryWrapperX<>();
        queryWrapper1.select(Model::getId)
                .eq(Model::getContractType, id)
                .eq(Model::getApproveStatus, 2)
                .eq(Model::getEffectStatus, 1).eq(Model::getEffective, 1)
                .orderByDesc(Model::getUpdateTime).last("LIMIT 20");
        List<Model> models = modelMapper.selectList(queryWrapper1);
        List<String> modelIdList = models.stream().map(Model::getId).collect(Collectors.toList());
        if (models.size() <= 3) {
            return getAiTemplateInfos(modelIdList);
        }

        // 使用 QueryWrapper 处理聚合函数
        QueryWrapper<ContractDO> queryWrapper = Wrappers.query();

        // 构建 Lambda 条件
        LambdaQueryWrapper<ContractDO> lambda = queryWrapper.lambda();

        // WHERE contract_type = 1
        lambda.eq(ContractDO::getContractType, id);

        // SELECT template_id, COUNT(1) AS count
        queryWrapper.select("template_id", "COUNT(1) AS count");

        // GROUP BY template_id
        queryWrapper.groupBy("template_id");

        // ORDER BY count DESC LIMIT 3
        queryWrapper.orderByDesc("count").last("LIMIT 3");
        List<ContractDO> contractDOS = contractMapper.selectList(queryWrapper);
        List<String> modelIdList2 = contractDOS.stream().filter(Objects::nonNull).map(ContractDO::getTemplateId).filter(Objects::nonNull).collect(Collectors.toList());

        //移除模板表查出来的数据
        modelIdList.removeAll(modelIdList2);

        //这里拼接上模板表查出来的数据
        modelIdList2.addAll(modelIdList);
        List<String> strings = modelIdList2.subList(0, Math.min(modelIdList2.size(), 3));
        return getAiTemplateInfos(strings);
    }

    @NotNull
    private List<AITemplateInfo> getAiTemplateInfos(List<String> collect) {

        if (CollectionUtil.isEmpty(collect)) {
            return new ArrayList<>(0);
        }

        List<Model> contractTemplateDOS = modelMapper.selectList(new LambdaQueryWrapperX<Model>().inIfPresent(Model::getId, collect));
        List<String> contractTypeIds = contractTemplateDOS.stream().filter(Objects::nonNull).map(Model::getContractType).filter(Objects::nonNull).collect(Collectors.toList());

        // 查询合同类型
        List<ContractType> contractTypeDOS = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().inIfPresent(ContractType::getId, contractTypeIds));
        Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeDOS, ContractType::getId);

        return contractTemplateDOS.stream().map(model -> {
            AITemplateInfo templateInfo = new AITemplateInfo();
            templateInfo.setId(model.getId());
            templateInfo.setName(model.getName());
            templateInfo.setContractType(model.getContractType());
            templateInfo.setRemoteFileId(model.getRemoteFileId());
            templateInfo.setRtfPdfFileId(model.getRtfPdfFileId());
            if (StringUtils.isEmpty(model.getContractType()) || contractTypeMap.get(model.getContractType()) == null) {
                templateInfo.setContractTypeName("");
            } else {
                templateInfo.setContractTypeName(contractTypeMap.get(model.getContractType()).getName());
            }
            return templateInfo;
        }).collect(Collectors.toList());
    }

}
