package com.yaoan.module.econtract.service.gpx.zcd;

import com.alibaba.fastjson.JSONObject;
import com.yaoan.module.econtract.api.gcy.zcd.IZcdApi;
import com.yaoan.module.econtract.api.gcy.zcd.dto.BankAccountDTO;
import com.yaoan.module.econtract.api.gcy.zcd.dto.BankAccountInfoDTO;
import com.yaoan.module.econtract.api.gcy.zcd.dto.ResponseDTO;
import com.yaoan.module.econtract.api.gcy.zcd.dto.ZcdCommonDTO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.SupplyBankReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.SupplyBankRespVO;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.util.gcy.EncryptUtil;
import com.yaoan.module.econtract.util.gcy.Sha256Util;
import com.yaoan.module.system.api.config.SystemConfigApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.Exceptions;

import javax.annotation.Resource;
import java.util.List;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @author doujiale
 */
@Service
@Slf4j
public class ZcdServiceImpl implements ZcdService {

    @Resource
    private IZcdApi zcdApi;
    @Resource
    private SystemConfigApi systemConfigApi;

    @Override
    public SupplyBankRespVO bankAccount(SupplyBankReqVO vo) throws Exception {
        String zcdToken = getZcdToken("zcd_config");
        ResponseDTO responseDTO = zcdApi.selectBankAccount(convertCommon(new BankAccountDTO().setSecondRoleCode(vo.getSupplyCode()).setPackageCode(vo.getPackageCode()).setProjectCode(vo.getProjectCode()).setAccess_token(zcdToken)));
        String data = responseDTO.getData();
        if (StringUtils.isEmpty(data)) {
            return new SupplyBankRespVO();
        }
        String desKey = EncryptUtil.decryptString(data, "12345678");
        List<BankAccountInfoDTO> bankAccountInfos = JSONObject.parseArray(desKey, BankAccountInfoDTO.class);
        if (CollectionUtils.isNotEmpty(bankAccountInfos)) {
            return new SupplyBankRespVO().setBankName(bankAccountInfos.get(0).getBankName()).setBankAccount(bankAccountInfos.get(0).getBankAccount());
        }
        return new SupplyBankRespVO();

    }

    private ZcdCommonDTO convertCommon(BankAccountDTO requestDTO) throws Exception {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String deData = EncryptUtil.encryptStringToBase64(JSONObject.toJSONString(requestDTO), "12345678");
        String mac = Sha256Util.encodeBySHA256(String.format("%s%s", timestamp, deData));
        return new ZcdCommonDTO().setData(deData).setApplnfo("Econtract").setMac(mac).setCreateDate(timestamp);
    }

    public String getZcdToken(String configKey) throws Exception {

        String configsByCKeys = systemConfigApi.getConfigByKey(configKey);
        if (StringUtils.isEmpty(configsByCKeys)) {
            throw Exceptions.propagate(exception(ErrorCodeConstants.EMPTY_DATA_ERROR));
        }
        //按照顺序切割
        String[] config = configsByCKeys.split("&");
        if (config.length < 4) {
            throw Exceptions.propagate(exception(ErrorCodeConstants.EMPTY_DATA_ERROR));
        }
        String oauthTokenStr = "";
        try {
            oauthTokenStr = zcdApi.gettoken(config[0], config[1], config[2], config[3]);
//            oauthTokenStr = zcdApi.gettoken("grant_type", "infoplatform", "guarantee_client", "response_type");
        } catch (Exception e) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "获取token异常，请检查账号配置信息");
        }
        JSONObject jsonObject = JSONObject.parseObject(oauthTokenStr);
        if (!"0000".equals(jsonObject.getString("code"))) {
            throw new Exception(jsonObject.getString("error_description"));
        }
        return jsonObject.getJSONObject("data").getString("access_token");
    }
}
