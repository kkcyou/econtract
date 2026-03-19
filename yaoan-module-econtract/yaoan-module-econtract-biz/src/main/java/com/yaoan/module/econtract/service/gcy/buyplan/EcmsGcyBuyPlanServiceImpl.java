package com.yaoan.module.econtract.service.gcy.buyplan;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoan.module.econtract.api.gcy.buyplan.SuperVisionApi;
import com.yaoan.module.econtract.dal.dataobject.gcy.buyplan.EcmsGcyBuyPlan;
import com.yaoan.module.econtract.dal.mysql.gcy.buyplan.EcmsGcyBuyPlanMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.gpmall.ContractGoodsMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.feign.IShuCaiApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.Exceptions;

import javax.annotation.Resource;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/3 11:54
 */
@Slf4j
@Service
public class EcmsGcyBuyPlanServiceImpl extends ServiceImpl<EcmsGcyBuyPlanMapper, EcmsGcyBuyPlan> implements EcmsGcyBuyPlanService{


    @Resource
    private IShuCaiApi shuCaiApi;
    @Resource
    private SuperVisionApi superVisionApi;
    @Resource
    private EcmsGcyBuyPlanMapper buyPlanMapper;

    @Resource
    private ContractGoodsMapper contractGoodsMapper;
    @Resource
    private SystemConfigApi systemConfigApi;

    @Override
    public String getInitToken(String configKey){
        //根据平台获取相对应的配置
        String configsByCKeys = systemConfigApi.getConfigByKey(configKey);
        if (StringUtils.isEmpty(configsByCKeys)) {
            throw Exceptions.propagate(exception(ErrorCodeConstants.EMPTY_DATA_ERROR));
        }
        //按照顺序切割
        String[] config = configsByCKeys.split("&");
        if(config.length < 5){
            throw Exceptions.propagate(exception(ErrorCodeConstants.EMPTY_DATA_ERROR));
        }
        String oauthTokenStr = "";
        try{
            oauthTokenStr = shuCaiApi.oauthCenterToken(config[0], config[1], config[2], config[3], config[4]);
        }catch (Exception e ){
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY,"获取token异常，请检查账号配置信息");
        }
        JSONObject jsonObject = JSONObject.parseObject(oauthTokenStr);
        if (jsonObject.get("error") != null) {
            try {
                throw new Exception(jsonObject.getString("error_description"));
            } catch (Exception e) {
                throw new RuntimeException(jsonObject.getString("error_description"));
            }
        }
        return jsonObject.getString("access_token");
    }
}
