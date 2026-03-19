package com.yaoan.module.econtract.service.hljsupply;

import com.alibaba.fastjson.JSONObject;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.contract.SupplyFromHLJDTO;
import com.yaoan.module.econtract.convert.hljsupply.HljSupplyConverter;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * Admin 用户 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class HLJSupplyServiceImpl implements HLJSupplyService {

    @Resource
    private ContractProcessApi contractProcessApi;

    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;

    @Override
    public SupplyFromHLJDTO getSupplyFromHLJ(String id) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("client_id", clientId);
        bodyParam.put("client_secret", clientSecret);
        String tokenObj = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
        JSONObject jsonObject = JSONObject.parseObject(tokenObj);
        if (jsonObject.get("error") != null) {
            throw exception(com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR, jsonObject.getString("error_description"));
        }
        String token = jsonObject.getString("access_token");
        return contractProcessApi.getSupplyById(token, id);
    }
    @Override
    public List<SupplyFromHLJDTO> getSupplyFromHLJ(List<String> ids) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("client_id", clientId);
        bodyParam.put("client_secret", clientSecret);
        String tokenObj = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
        JSONObject jsonObject = JSONObject.parseObject(tokenObj);
        if (jsonObject.get("error") != null) {
            throw exception(com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR, jsonObject.getString("error_description"));
        }
        String token = jsonObject.getString("access_token");
        return contractProcessApi.getSupplyByIds(token, ids);
    }

    @Override
    public SupplyDTO getSupply(String id) {
        return HljSupplyConverter.INSTANCE.convertSingleton(getSupplyFromHLJ(id));
    }

    @Override
    public List<SupplyDTO> getSupplyList(List<String> ids) {
        return HljSupplyConverter.INSTANCE.convertList(getSupplyFromHLJ(ids));

    }
}
