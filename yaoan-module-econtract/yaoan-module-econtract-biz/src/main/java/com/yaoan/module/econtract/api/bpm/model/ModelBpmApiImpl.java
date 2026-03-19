package com.yaoan.module.econtract.api.bpm.model;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.SimpleTaskDTO;
import com.yaoan.module.bpm.enums.model.FlowableModelEnums;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ModelCreateReqDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.TermsAddVO;
import com.yaoan.module.econtract.controller.admin.model.vo.ModelCreateReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.ModelTermsAddVO;
import com.yaoan.module.econtract.controller.admin.model.vo.TermsDetailsVo;
import com.yaoan.module.econtract.controller.admin.term.vo.TermAddVO;
import com.yaoan.module.econtract.convert.model.ModelConverter;
import com.yaoan.module.econtract.convert.term.TermConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.model.ModelBpmDO;
import com.yaoan.module.econtract.dal.dataobject.catalog.ModelCatalogDo;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.term.ModelTerm;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.mysql.bpm.model.ModelBpmMapper;
import com.yaoan.module.econtract.dal.mysql.catalog.ModelCatalogMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.term.ModelTermMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/14 11:54
 */
@Service
public class ModelBpmApiImpl implements ModelBpmApi {
    @Resource
    private ModelBpmMapper modelBpmMapper;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private ModelCatalogMapper modelCatalogMapper;
    @Resource
    private ModelTermMapper modelTermMapper;
    @Resource
    private TermMapper termMapper;
    @Resource
    private ContractProcessApi contractProcessApi;
    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;

    @Override
    @DataPermission(enable = false)
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {
        ModelBpmDO modelBpmDO = modelBpmMapper.selectById(businessKey);
        if (modelBpmDO != null) {
            //1.更新模板审批流表状态
            modelBpmDO.setResult(statusEnums.getResult());
            modelBpmMapper.updateById(modelBpmDO);
            //2.更新模板审批状态
            Model model = modelMapper.selectById(modelBpmDO.getModelId());
            if (model != null) {
                if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                    model.setApproveTime(LocalDateTime.now());
                    model.setApproveResultTime(LocalDateTime.now());

                }

                if (BpmProcessInstanceResultEnum.PROCESS == statusEnums) {

                    //判断是否是再次提交
                    if (ifReSubmit(modelBpmDO)) {
                        model.setSubmitTime(LocalDateTime.now());
                    }
                }
                model.setApproveStatus(statusEnums.getResult());
                //同步审批状态
                modelMapper.updateById(model.setApproveStatus(statusEnums.getResult()));
                //终审通过后将模版推送到黑龙江
                if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                    if (model.getIsGov().equals(1)) {
                        ModelCreateReqVO reqVO = ModelConverter.INSTANCE.toModelCreateReqVO(model);

                        //查询是否有品目
                        List<ModelCatalogDo> catalogList = modelCatalogMapper.selectList(new LambdaQueryWrapperX<ModelCatalogDo>()
                                .eq(ModelCatalogDo::getModelId, model.getId()));
                        if (ObjectUtil.isNotEmpty(catalogList)) {
                            //将catalogGuid提取成list
                            List<String> catalogGuidList = catalogList.stream().map(ModelCatalogDo::getCatalogGuid).collect(Collectors.toList());
                            reqVO.setPurCatalogGuid(catalogGuidList);
                        }
                        //模板关联条款表
                        List<ModelTerm> modelTerms = modelTermMapper.selectList(new LambdaQueryWrapperX<ModelTerm>()
                                .eq(ModelTerm::getModelId, model.getId()));
                        if (ObjectUtil.isNotEmpty(modelTerms)) {
                            List<ModelTermsAddVO> newModelTerms = new ArrayList<>();
                            modelTerms.forEach(item -> {
                                newModelTerms.add(new ModelTermsAddVO().setTermId(item.getTermId()).setTermNum(item.getTermNum()).setTitle(item.getTitle()).setName(item.getName()).setEditable(item.getEditable()));
                            });
                            reqVO.setTerms(newModelTerms);
                        }
                        // 条款表信息
                        List<String> termIds = modelTerms.stream().map(ModelTerm::getTermId).collect(Collectors.toList());
                        if (CollectionUtil.isNotEmpty(termIds)){
                            List<Term> terms = termMapper.selectList(Term::getId, termIds);
                            List<TermsAddVO> termAddVOS = TermConverter.INSTANCE.convertAddList(terms);
                            reqVO.setTermsInfo(termAddVOS);
                        }
                        //同步合同到电子合同
                        Map<String, Object> bodyParam = new HashMap<>();
                        bodyParam.put("client_id", clientId);
                        bodyParam.put("client_secret", clientSecret);
                        String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                        JSONObject jsonObject = JSONObject.parseObject(token);
                        if (jsonObject.get("error") != null) {
                            throw exception(500,"获取token异常：，"+jsonObject.getString("error"));

//                            try {
//                                throw new Exception(jsonObject.getString("error_description"));
//                            } catch (Exception e) {
//                                throw new RuntimeException(jsonObject.getString("error_description"));
//                            }
                        }
                        
                            ModelCreateReqDTO dto = ModelConverter.INSTANCE.toDTO(reqVO);
                            dto.setId(null);
                            String result = contractProcessApi.insertModelByUpload(jsonObject.getString("access_token"), dto.setId(model.getId()));
                            System.out.println(result);
                            JSONObject resultJson = JSONObject.parseObject(result);
                            if (!"0".equals(resultJson.getString("code"))) {
                                throw exception(500,"模板发送失败，"+resultJson.getString("msg"));
                            }
                       
                    }
                }
            }
        }
    }

    /**
     * 判断是否是再次提交
     */
    private Boolean ifReSubmit(ModelBpmDO modelBpmDO) {
        //最新的待办任务是update，就是再次提交
        List<String> processIds = new ArrayList<String>();
        processIds.add(modelBpmDO.getProcessInstanceId());
        List<SimpleTaskDTO> taskDTOList = bpmTaskApi.getAllTaskInfoByProcessIds(processIds);
        if (CollectionUtil.isNotEmpty(taskDTOList)) {
            // 找出 最新的任务 的元素
            SimpleTaskDTO latestTaskDTO = taskDTOList.stream()
                    .max(Comparator.comparing(SimpleTaskDTO::getEndTime))
                    .orElse(null);
            if (ObjectUtil.isNotNull(latestTaskDTO)) {
                if (FlowableModelEnums.UPDATE.getName().equals(latestTaskDTO.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}

