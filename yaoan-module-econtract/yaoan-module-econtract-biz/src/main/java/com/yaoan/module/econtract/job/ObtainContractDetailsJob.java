package com.yaoan.module.econtract.job;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.common.util.json.JsonUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.quartz.core.handler.JobHandler;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.api.contract.SppGPTApi;
import com.yaoan.module.econtract.api.contract.dto.SppGPTDetailDTO;
import com.yaoan.module.econtract.api.contract.dto.SppGPTResponseDTO;
import com.yaoan.module.econtract.controller.admin.contract.vo.Contract;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractDetailRespVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDetailsDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractDetailsMapper;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.econtract.service.intelligentReview.IntelligentReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component("ObtainContractDetailsJob")
public class ObtainContractDetailsJob implements JobHandler {

    @Resource
    private SppGPTApi sppGPTApi;
    @Resource
    private ContractDetailsMapper contractDetailsMapper;
    @Resource
    private IntelligentReviewService intelligentReviewService;

    @Override
    @TenantIgnore
    public String execute(String param) throws Exception {

        log.debug("执行了获取合同解析详情任务～");
        //查询待解析的合同
        List<ContractDetailsDO> contractDetailsDOS = contractDetailsMapper.selectList(new LambdaQueryWrapperX<ContractDetailsDO>().eq(ContractDetailsDO::getStatus, 0));
        if (CollectionUtil.isNotEmpty(contractDetailsDOS)) {
            //获取taskId
            List<String> taskIds = contractDetailsDOS.stream().map(ContractDetailsDO::getId).collect(Collectors.toList());
            //将集合转换成"2024001，2024002"格式
            String taskIdStr = String.join(",", taskIds);
            String token = "Bearer " + intelligentReviewService.generateToken().getToken();
            SppGPTDetailDTO sppGPTDetailDTO = new SppGPTDetailDTO().setTaskIds(taskIdStr);
            SppGPTResponseDTO sppGPTResponseDTO = sppGPTApi.contractDetail(token, sppGPTDetailDTO);
            HashMap map = sppGPTResponseDTO.getData();
            String jsonString = JsonUtils.toJsonString(map);
            ContractDetailRespVO contractDetailRespVO = JsonUtils.parseObject(jsonString, ContractDetailRespVO.class);
            List<Contract> contracts = null;
            if (contractDetailRespVO != null) {
                contracts = contractDetailRespVO.getContracts();
            }
            List<ContractDetailsDO> updateList = new ArrayList<>();
            if (contracts != null) {
                for (Contract contract : contracts) {
                    if (Objects.equals(contract.getStatus(), "1")) {
                        ContractDetailsDO contractDetailsDO = new ContractDetailsDO()
                                .setId(contract.getTaskId())
                                .setStatus(contract.getStatus())
                                .setContractBaseInfo(JsonUtils.toJsonString(contract.getContractBaseInfo()))
                                .setPurchaseDetail(JsonUtils.toJsonString(contract.getPurchaseDetail()))
                                .setSuppliers(JsonUtils.toJsonString(contract.getSuppliers()))
                                .setSubSuppliers(JsonUtils.toJsonString(contract.getSubSuppliers()))
                                .setPaymentPlan(JsonUtils.toJsonString(contract.getPaymentPlan()))
                                .setAcceptRequirement(JsonUtils.toJsonString(contract.getAcceptRequirement()))
                                .setProjectManager(JsonUtils.toJsonString(contract.getProjectManager()));
                        updateList.add(contractDetailsDO);
//                        contractDetailsMapper.update(contractDetailsDO, new LambdaQueryWrapperX<ContractDetailsDO>().eq(ContractDetailsDO::getId, contract.getTaskId()));
                    }
                }
            }
            contractDetailsMapper.updateBatch(updateList);
            return "执行获取合同解析详情任务完成";
        }
        return "没有待解析的合同~~";
    }

}
