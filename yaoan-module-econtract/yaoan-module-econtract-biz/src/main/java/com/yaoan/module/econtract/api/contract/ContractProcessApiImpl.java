package com.yaoan.module.econtract.api.contract;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaoan.framework.common.exception.ErrorCode;
import com.yaoan.module.econtract.api.contract.dto.*;
import com.yaoan.module.econtract.api.contract.dto.gcy.CancellationFileDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ModelCreateReqDTO;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import com.yaoan.module.econtract.api.model.dto.ModelDTO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import com.yaoan.module.system.api.config.SystemConfigApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * ContractProcessApi 实现类
 *
 * @author lls
 */
@Service
@Slf4j
public class ContractProcessApiImpl implements ContractProcessApi {

    @Resource
    ContractProcessFeignApi contractProcessFenApi;
    @Resource
    SystemConfigApi systemConfigApi;

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ContractTypeMapper contractTypeMapper;

    @Override
    public String oauthCenterToken(Map<String, Object> paramMap, String grantType, String client_id, String client_secret, String username, String password) {
        
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{access_token:\"\"}";
        }
        String key = "plat_token:heilongjiang";
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key)) || !stringRedisTemplate.opsForValue().get(key).contains("access_token")) {
            String token = contractProcessFenApi.oauthCenterToken(paramMap, grantType, client_id, client_secret, username, password);
            stringRedisTemplate.opsForValue().set(key, token, 86400, TimeUnit.SECONDS);
        }
        return stringRedisTemplate.opsForValue().get(key);
    }
    public void checkReturn(String result){
        JSONObject resultJson = JSONObject.parseObject(result);
        String message = resultJson.getString("msg");
        if(StringUtils.isEmpty(message)){
            message =  resultJson.getString("message");
        }
        if (!"0".equals(resultJson.getString("code"))) {
            throw exception(new ErrorCode(500,message));
        }
    }
    @Override
    public String sendContract(String accessToken, EncryptDTO encryptDTO) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        String result = contractProcessFenApi.sendContract(accessToken, encryptDTO);
        checkReturn(result);
        return result;
    }

    @Override
    public String backContract(String accessToken, EncryptDTO encryptDTO) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        String result = contractProcessFenApi.backContract(accessToken, encryptDTO);
        checkReturn(result);
        return result;
    }

    @Override
    public String pushContractToEcontract(String accessToken, EncryptDTO encryptDTO) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        String result = contractProcessFenApi.pushContractToEcontract(accessToken, encryptDTO);
        checkReturn(result);
        return result;
    }

    @Override
    public String pushGPXContractToEcontract(String accessToken, EncryptDTO encryptDTO) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        String result = contractProcessFenApi.pushGPXContractToEcontract(accessToken, encryptDTO);
        checkReturn(result);
        return result;
    }

    @Override
    public String contractUpload(String accessToken, ContractFileDTO contractFileDTO) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        String result = contractProcessFenApi.contractUpload(accessToken, contractFileDTO);
        checkReturn(result);
        return result;
    }

    @Override
    public String pushSignContract(String accessToken, EncryptDTO encryptDTO) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        String result = contractProcessFenApi.pushSignContract(accessToken, encryptDTO);
        checkReturn(result);
        return result;
    }

    @Override
    public String insertModelByUpload(String accessToken, ModelCreateReqDTO dto) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        enhanceContractType4HLJ(dto);
        String result = contractProcessFenApi.insertModelByUpload(accessToken, dto);
        checkReturn(result);
        return result;
    }

    private void enhanceContractType4HLJ(ModelCreateReqDTO dto) {
        ContractType contractType = contractTypeMapper.selectById(dto.getContractType());
        if(ObjectUtil.isNotNull(contractType)){
            dto.setContractType(contractType.getPlatId());
        }


    }

    @Override
    public String unitSyncContractStatus(String accessToken, SyncContractStatusDTO dto) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        String result = contractProcessFenApi.unitSyncContractStatus(accessToken, dto);
        checkReturn(result);
        return result;
    }

    @Override
    public String invalidateContractById(String accessToken, IdReqDTO dto) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        String result = contractProcessFenApi.invalidateContractById(accessToken, dto);
        checkReturn(result);
        return result;
    }

    @Override
    public String cancelContract(String accessToken, CancellationFileDTO dto) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        String result = contractProcessFenApi.cancelContract(accessToken, dto);
        checkReturn(result);
        return result;
    }

    @Override
    public String updateOrderStatus(String accessToken, DraftOrderInfo draftOrderInfo) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        String result = contractProcessFenApi.updateOrderStatus(accessToken, draftOrderInfo);
        checkReturn(result);
        return result;
    }

    @Override
    public String updateStatusPackage(String accessToken, PackageUpdateDTO packageUpdateDTO) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        String result = contractProcessFenApi.updateStatusPackage(accessToken, packageUpdateDTO);
        checkReturn(result);
        return result;
    }

    @Override
    public String openProductOrg(String accessToken, ProductOrgDTO productOrgDTO) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return "{code:\"0\"}";
        }
        EncryptDTO encryptDTO = null;
        try {
            encryptDTO = Sm4Utils.convertParam(productOrgDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String result = contractProcessFenApi.openProductOrg(accessToken, encryptDTO);
        checkReturn(result);
        return result;
    }

    @Override
    public List<ModelDTO> getModelListByOrgId(String accessToken, String orgId) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return  new ArrayList<>();
        }
        EncryptDTO encryptDTO = null;
        try {
            Map map = new HashMap();
            map.put("orgId", orgId);
            encryptDTO = Sm4Utils.convertParam(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String result = contractProcessFenApi.getModelListByOrgId(accessToken, encryptDTO);
        checkReturn(result);
        JSONObject resultJson = JSONObject.parseObject(result);
        String data = resultJson.getString("data");
        String s = null;
        try {
            s = Sm4Utils.decryptEcb(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<ModelDTO> models = JSONObject.parseArray(s, ModelDTO.class);
        return models;
    }

    @Override
    public UserDTO getUserByOrgId(String accessToken, String orgId) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return new UserDTO();
        }
        EncryptDTO encryptDTO = null;
        try {
            Map map = new HashMap();
            map.put("orgId", orgId);
            encryptDTO = Sm4Utils.convertParam(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String result = contractProcessFenApi.getUserByOrgId(accessToken, encryptDTO);
        checkReturn(result);
        JSONObject resultJson = JSONObject.parseObject(result);
        String data = resultJson.getString("data");
        String s = null;
        try {
            s = Sm4Utils.decryptEcb(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserDTO userDTO = JSONObject.parseObject(s, UserDTO.class);

        return userDTO;
    }

    @Override
    public SupplyFromHLJDTO getSupplyById(String accessToken, String supplyId) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return null;
        }
        EncryptDTO encryptDTO = null;
        try {
            Map map = new HashMap();
            map.put("supplyId", supplyId);
            encryptDTO = Sm4Utils.convertParam(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String result = contractProcessFenApi.getSupplyById(accessToken, encryptDTO);
        checkReturn(result);
        JSONObject resultJson = JSONObject.parseObject(result);
        String data = resultJson.getString("data");
        String s = null;
        try {
            s = Sm4Utils.decryptEcb(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SupplyFromHLJDTO supply = JSONObject.parseObject(s, SupplyFromHLJDTO.class);
        return supply;    }

    @Override
    public List<SupplyFromHLJDTO> getSupplyByIds(String accessToken, List<String> supplyIds) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return null;
        }
        EncryptDTO encryptDTO = null;
        try {
            Map map = new HashMap();
            map.put("supplyIds", supplyIds);
            encryptDTO = Sm4Utils.convertParam(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String result = contractProcessFenApi.getSupplyByIds(accessToken, encryptDTO);
        checkReturn(result);
        JSONObject resultJson = JSONObject.parseObject(result);
        String data = resultJson.getString("data");
        String s = null;
        try {
            s = Sm4Utils.decryptEcb(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<SupplyFromHLJDTO> supply = JSONObject.parseArray(s, SupplyFromHLJDTO.class);
        return supply;
    }


    @Override
    public EcontractOrgDTO getEcontractOrgById(String accessToken, String orgId) {
        String cvalue  = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if("n".equals(cvalue)){
            return null;
        }
        EcontractOrgDTO  econtractOrg = new EcontractOrgDTO();
        econtractOrg.setId(orgId);
        String result = contractProcessFenApi.getEcontractOrgByOrgId(accessToken, econtractOrg);
        checkReturn(result);
        JSONObject resultJson = JSONObject.parseObject(result);
        String data = resultJson.getString("data");
        EcontractOrgDTO econtractOrgDTO = JSONObject.parseObject(data, EcontractOrgDTO.class);

        return econtractOrgDTO;
    }
}
