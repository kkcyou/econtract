package com.yaoan.framework.sms.core.client.impl.shanxi;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaoan.framework.common.core.KeyValue;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.sms.core.client.SmsCommonResult;
import com.yaoan.framework.sms.core.client.dto.SmsReceiveRespDTO;
import com.yaoan.framework.sms.core.client.dto.SmsSendRespDTO;
import com.yaoan.framework.sms.core.client.dto.SmsTemplateRespDTO;
import com.yaoan.framework.sms.core.client.impl.AbstractSmsClient;
import com.yaoan.framework.sms.core.enums.SmsTemplateAuditStatusEnum;
import com.yaoan.framework.sms.core.property.SmsChannelProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class ShanXiSmsClient extends AbstractSmsClient {

    /**
     * 获取token相关参数
     */
    private String tokenUrl; //获取token的url
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String username;
    private String password;

    //发送短信相关参数
    private String sendUrl; //调用发送短信的地址
    //发送短信的业务码，一个业务码对应一个模板，所以电子合同都是使用的同一个模板
    private String businessCode;
    // 发送短信的type  目前固定为7
    private int msgType;


    public ShanXiSmsClient(SmsChannelProperties properties) {
        super(properties, new ShanxiSmsCodeMapping());
        Assert.notEmpty(properties.getApiKey(), "apiKey 不能为空");
        Assert.notEmpty(properties.getApiSecret(), "apiSecret 不能为空");
    }
    @Override
    protected void doInit() {
        Properties properties = new Properties();
        try {
            InputStream inputStream =  ShanXiSmsClient.class.getClassLoader().getResourceAsStream("sms/shanxi.properties");
            properties.load(inputStream);
            tokenUrl = properties.getProperty("tokenUrl");
            clientId = properties.getProperty("clientId");
            clientSecret = properties.getProperty("clientSecret");
            grantType = properties.getProperty("grantType");
            username = properties.getProperty("username");
            password = properties.getProperty("password");

            sendUrl = properties.getProperty("sendUrl");
            businessCode = properties.getProperty("businessCode");
            msgType = Integer.parseInt(properties.getProperty("msgType"));

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected SmsCommonResult<SmsSendRespDTO> doSendSms(Long sendLogId, String mobile, String apiTemplateId, List<KeyValue<String, Object>> templateParams,String content) throws Throwable {

        RedisUtils redisUtils = SpringUtil.getBean(RedisUtils.class);
        Map<String, Object> responseObj = new HashMap<>();
        String token = redisUtils.get("smsToken");
        if (token == null || "".equals(token) ){
            token = getToken(tokenUrl,clientId,clientSecret,grantType,username,password);
            if(token == null || "".equals(token)){
                throw new RuntimeException("发送短信失败！获取token异常");
            }
            redisUtils.set("smsToken", token, 7000);
        }
        try {
            Map<String,Object> sendParam = new HashMap<>();
            sendParam.put("businessCode",businessCode);
            sendParam.put("msgType",7);
            sendParam.put("phone",mobile);
            Map<String,Object> templateMap = new HashMap<>();
            content = content.replaceAll(",","，");
            templateMap.put("msgParams",content);
            sendParam.put("msg0",templateMap);
            sendParam.put("msg1",new HashMap<>());
            HttpRequest httpRequest = HttpUtil.createRequest(Method.POST,sendUrl);
            String str  = JSONUtil.toJsonStr(sendParam);
            httpRequest.body(str);
            httpRequest.timeout(HttpGlobalConfig.getTimeout());
            httpRequest.header("access_token",token);

            String responseText = httpRequest.execute().body();
            log.info(responseText);
            //String responseText = HttpUtil.post(sendUrl, sendParam);

            ObjectMapper objectMapper = new ObjectMapper();

            responseObj = objectMapper.readValue(responseText,Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("发送短信失败，调用发送接口异常");
        }
        return SmsCommonResult.build(MapUtil.getStr(responseObj, "code"), MapUtil.getStr(responseObj, "msg"),
                null, new SmsSendRespDTO().setSerialNo(StrUtil.uuid()), codeMapping);
    }

    protected  SmsCommonResult<SmsSendRespDTO> doSendSms2(Long sendLogId, String mobile, String apiTemplateId, List<KeyValue<String, Object>> templateParams)  {


        return null ;
    }

    public static void main(String[] args) {
        //doSendSms2(null,"18756169117",null,null);
        //getToken();
    }
    private  String getToken(String url, String client_id, String client_secret, String grant_type, String username, String password)  {

        Map<String, Object> params = new HashMap<>();
        params.put("client_id",client_id);
        params.put("client_secret",client_secret);
        params.put("grant_type",grant_type);
        params.put("username",username);
        params.put("password",password);
        Map<String, Object> responseObj = new HashMap<>();
        try {
            String  responseText = HttpUtil.post(url, params);
            log.info(responseText);
            ObjectMapper objectMapper = new ObjectMapper();
           
           
            responseObj = objectMapper.readValue(responseText,Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("发送短信失败！获取token异常");
        } 


        return (String) responseObj.get("access_token");
    }

    @Override
    protected List<SmsReceiveRespDTO> doParseSmsReceiveStatus(String text) throws Throwable {
        return null;
    }

    @Override
    protected SmsCommonResult<SmsTemplateRespDTO> doGetSmsTemplate(String apiTemplateId) throws Throwable {
        SmsTemplateRespDTO data = new SmsTemplateRespDTO().setId(apiTemplateId).setContent("")
                .setAuditStatus(SmsTemplateAuditStatusEnum.SUCCESS.getStatus()).setAuditReason("");
        return SmsCommonResult.build("200", "success", null, data, codeMapping);
    }
}
