package com.yaoan.module.econtract.job.contractBak;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yaoan.framework.common.util.json.JsonUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.quartz.core.handler.JobHandler;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.contract.dto.ContractArchiveStateRespDTO;
import com.yaoan.module.econtract.api.contract.dto.ContractStatusDTO;
import com.yaoan.module.econtract.api.contract.dto.SyncContractStatusDTO;
import com.yaoan.module.econtract.api.contract.dto.mongolia.EncryptResponseDto;
import com.yaoan.module.econtract.api.contract.dto.mongolia.MedicalResponseDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.MedicalApi;
import com.yaoan.module.econtract.api.gcy.buyplan.SuperVisionApi;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractArchiveStateDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ModelCreateReqDTO;
import com.yaoan.module.econtract.convert.model.ModelConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.statistics.StatisticsDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.statistics.StatisticsMapper;
import com.yaoan.module.econtract.enums.BusinessTokenConfigEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.gcy.gpmall.ContractArchiveStateEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.ContractDrafterEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.service.gcy.buyplan.EcmsGcyBuyPlanServiceImpl;
import com.yaoan.module.econtract.service.workday.WorkDayService;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.config.dto.SystemConfigRespDTO;
import com.yaoan.module.system.api.region.RegionApi;
import com.yaoan.module.system.api.region.dto.RegionDTO;
import com.yaoan.module.system.api.user.OrganizationApi;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.OutServiceExceptionUtil.outException;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @author doujl
 */
@Slf4j
@Component("archiveStatusJob")
public class ArchiveStatusJob implements JobHandler {

//    @Resource
//    private ContractOrderExtDO orderContractMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private EcmsGcyBuyPlanServiceImpl gcyBuyPlanService;
    @Resource
    private SuperVisionApi superVisionApi;
    @Resource
    private RegionApi regionApi;
    @Resource
    private MedicalApi medicalApi;
    @Resource
    private WorkDayService workDayService;
    @Resource
    private StatisticsMapper statisticsMapper;
    @Resource
    private OrganizationApi orgApi;
    @Resource
    private ContractOrderExtMapper orderContractExtMapper;
    @Resource
    private ContractProcessApi contractProcessApi;
    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;

    @Override
    @TenantIgnore
    public String execute(String param) throws Exception {
        long now = System.currentTimeMillis();
        System.out.println("【定时任务】【备案中>>已备案】执行查询合同备案状态的定时任务～");
        try {
            // 查询所有在备案中和已备案的合同
            List<ContractOrderExtDO> contractList = orderContractExtMapper.selectList(new LambdaQueryWrapperX<ContractOrderExtDO>()
                    .eq(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode())
                    .orderByDesc(ContractOrderExtDO::getCreateTime)
                    .select(ContractOrderExtDO::getId, ContractOrderExtDO::getPdfFileId, ContractOrderExtDO::getStatus,
                            ContractOrderExtDO::getPlatform,ContractOrderExtDO::getContractSignTime,ContractOrderExtDO::getBuyerOrgId
//                           ,ContractDO::getOrderId,ContractDO::getContractDrafter, ContractDO::getSupSinTime,
//                             ContractDO::getOrgSinTime
                    ));
//            List<ContractDO> contractList = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>()
////                    .in(ContractDO::getPlatform, PlatformEnums.JDMALL.getCode(), PlatformEnums.ZHUBAJIE.getCode(),
////                            PlatformEnums.GPMS_GPX.getCode())
//                    .eq(ContractDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode())
//                    .orderByDesc(ContractDO::getCreateTime)
//                    .select(ContractDO::getId, ContractDO::getPdfFileId, ContractDO::getStatus,
//                            ContractDO::getPlatform,ContractDO::getContractSignTime,ContractDO::getBuyerOrgId
////                           ,ContractDO::getOrderId,ContractDO::getContractDrafter, ContractDO::getSupSinTime,
////                             ContractDO::getOrgSinTime
//                    ));
            String accessToken = gcyBuyPlanService.getInitToken(BusinessTokenConfigEnums.SUPER_CONTROL.getInfo());
            if (CollectionUtils.isNotEmpty(contractList)) {
                System.out.println("【定时任务】【备案中>>已备案】执行条数" + contractList.size());
                List<ContractOrderExtDO> updateList = new ArrayList<>();
                String cValue = getCvalueByKey(SystemConfigKeyEnums.IF_NEW_JIANGUAN.getKey());
                for (ContractOrderExtDO orderContractDO : contractList) {
                    EncryptResponseDto response = null;
                    MedicalResponseDTO medicalResponseDTO = null;
                    ContractArchiveStateDTO contractReqDTO = new ContractArchiveStateDTO().setContractGuid(orderContractDO.getId()).setPlatform(orderContractDO.getPlatform());
                    EncryptDTO encryptDTO = Sm4Utils.convertParam(contractReqDTO);
                    //是否需要走监管新版本的接口(融通平台备案接口)  n：否  y：是
                    String orgGuid = "";
                    String regionCode = "";
                    if ("y".equals(cValue)) {
                        //获取采购单位ID和采购单位区划
                        if (ObjectUtil.isEmpty(orderContractDO)) {
                            throw outException(ErrorCodeConstants.DATA_MISSING, "合同不存在");
                        }
                        orgGuid = orderContractDO.getBuyerOrgId();
                        OrganizationDTO organization = orgApi.getOrganization(orgGuid);
                        if (ObjectUtil.isNotNull(organization)) {
                            RegionDTO region = organization.getRegionGuid() == null ? null : regionApi.getRegionById(organization.getRegionGuid());
                            regionCode = region == null ? null : region.getRegionCode();
                        }
                        String archiveState = superVisionApi.getContractArchiveStateV4(accessToken, orgGuid, regionCode, encryptDTO);
                        System.out.println("【定时任务】【备案中>>已备案】执行接口返回数据"+archiveState);
                        response = JSONObject.parseObject(archiveState, EncryptResponseDto.class);

                    }
                    if (ObjectUtil.isNotEmpty(response)) {
                        if (response.getStatus().equals("0")) {
                            if (ObjectUtil.isNotEmpty(response.getData())) {
                                ContractArchiveStateRespDTO result = JSONObject.parseObject(Sm4Utils.decryptEcb(response.getData()), ContractArchiveStateRespDTO.class);
                                //如果合同备案状态不为空，并且状态为已备案，则更新备案中合同状态为已备案
                                if (ObjectUtil.isNotEmpty(result)) {
                                    if (Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BAKED.getCode()) && Objects.equals(orderContractDO.getStatus(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode())) {
                                        orderContractDO.setStatus(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode());
//                                        fillData(orderContractDO);
//                                        orderContractDO.setIsFilings(2);
                                        updateList.add(orderContractDO);
                                        //调平台接口推送合同信息
//                                        contractPublisher.sendContractDataEvent(new ContractDOEvent(this).setContractId(orderContractDO.getId()));
//                                        gpMallContractService.sendEcmsOperationLogEvent(orderContractDO, null, null);
                                    }
                                }
                            }
                        }
                    }
                    if (ObjectUtil.isNotEmpty(medicalResponseDTO)) {
                        if (medicalResponseDTO.getCode().equals("200")) {
                            ContractArchiveStateRespDTO result = null;
                            if (ObjectUtil.isNotEmpty(medicalResponseDTO.getData())) {
                                try {
                                    HashMap map = medicalResponseDTO.getData();
                                    String jsonString = JsonUtils.toJsonString(map);
                                    // 解析JSON字符串
                                    result = JSONObject.parseObject(String.valueOf(jsonString), ContractArchiveStateRespDTO.class);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (ObjectUtil.isNotEmpty(result)) {
                                    if (Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BAKED.getCode()) && Objects.equals(orderContractDO.getStatus(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode())) {
                                        orderContractDO.setStatus(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode());
//                                        orderContractDO.setIsFilings(2);
                                        updateList.add(orderContractDO);
                                        //调平台接口推送合同信息
//                                        contractPublisher.sendContractDataEvent(new ContractDOEvent(this).setContractId(orderContractDO.getId()));
//                                        gpMallContractService.sendEcmsOperationLogEvent(orderContractDO, null, null);
                                    }
                                }
                            }
                        }
                    }

                }
                if(CollectionUtils.isNotEmpty(updateList)){
                    orderContractExtMapper.updateBatch(updateList);
                    //修改合同表备案状态
                    List<String> ids = updateList.stream().map(ContractOrderExtDO::getId).collect(Collectors.toList());
                    List<ContractDO> contractDOList = new ArrayList<>();
                    ids.forEach(id -> {
                        ContractDO contractDO = new ContractDO();
                        contractDO.setId(id);
                        contractDO.setIsFilings(2);
                        contractDOList.add(contractDO);
                    });
                    contractMapper.updateBatch(contractDOList);
                    //合同状态同步给黑龙江
                    Map<String, Object> bodyParam = new HashMap<>();
                    bodyParam.put("client_id", clientId);
                    bodyParam.put("client_secret", clientSecret);
                    String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                    JSONObject jsonObject = JSONObject.parseObject(token);
                    if (jsonObject.get("error") != null) {
                        throw exception(ErrorCodeConstants.DIY_ERROR, jsonObject.getString("error_description"));
                    }

                    try {
                        List<ContractStatusDTO> list = updateList.stream()
                                .map(orderContractDO -> new ContractStatusDTO()
                                        .setContractGuid(orderContractDO.getId())
                                        .setStatus(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode()))
                                .collect(Collectors.toList());
                        SyncContractStatusDTO dto = new SyncContractStatusDTO().setContractList(list);
                        String result =  contractProcessApi.unitSyncContractStatus(jsonObject.getString("access_token"),dto);
                        System.out.println(result);
                        JSONObject resultJson = JSONObject.parseObject(result);
                        if (!"0".equals(resultJson.getString("code"))) {
                            throw exception(ErrorCodeConstants.DIY_ERROR, "同步状态失败");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("【定时任务】【备案中>>已备案】定时任务执行异常" + e.getMessage());
            return e.getMessage();
        }
        System.out.println("【定时任务】【备案中>>已备案】定时任务执行完成，耗时：" + (System.currentTimeMillis() - now) + "ms");
        return "执行合同备案状态查询的定时任务完成";
    }
    private String getCvalueByKey(String key) {
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(key);
        List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
        String cValue = configsByCKeys.size()==0?null:configsByCKeys.get(0).getCValue();
        return cValue;
    }
    private void fillData(ContractDO orderContractDO) throws Exception {
        //判断是否超期
        try {
            Date supSinTime = new Date();
            LambdaUpdateWrapper<StatisticsDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            if(orderContractDO.getPlatform().equals(PlatformEnums.GPMS_GPX.getCode())){
//                 supSinTime = orderContractDO.getSupSinTime();
            } else if (orderContractDO.getPlatform().equals(PlatformEnums.GPMALL.getCode()) || orderContractDO.getPlatform().equals(PlatformEnums.GP_GPFA.getCode())) {
//                if(orderContractDO.getOrgSinTime() != null){
//                    supSinTime = orderContractDO.getOrgSinTime();
//                } else {
//                    supSinTime = orderContractDO.getContractSignTime();
//                }
            } else if (orderContractDO.getPlatform().equals(PlatformEnums.JDMALL.getCode()) || orderContractDO.getPlatform().equals(PlatformEnums.ZHUBAJIE.getCode())){
//                if(orderContractDO.getContractDrafter().equals(ContractDrafterEnums.ORG_SEND.getCode())){
//                    supSinTime = orderContractDO.getSupSinTime();
//                }else {
//                    supSinTime = orderContractDO.getOrgSinTime();
//                }
            }
            String warningDate = workDayService.getWarningDate(1, 0, 7, LocalDate.now());
            if (StringUtils.isNotEmpty(warningDate) && supSinTime != null && supSinTime.before(DateUtil.parse(warningDate))) {
                lambdaUpdateWrapper.set(StatisticsDO::getBakFlag, 1);
            } else {
                lambdaUpdateWrapper.set(StatisticsDO::getBakFlag, 0);
            }
            //数据统计表修改合同状态
            lambdaUpdateWrapper.set(StatisticsDO::getBakDate, new Date());
            lambdaUpdateWrapper.set(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode());
            lambdaUpdateWrapper.eq(StatisticsDO::getContractId, orderContractDO.getId());
            statisticsMapper.update(null,lambdaUpdateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String calculateSign(String noise, String key) {
        try {
            String toHash = noise + "##" + key;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(toHash.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
