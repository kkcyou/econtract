package com.yaoan.framework.security.core.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yaoan.framework.security.config.ClientInfo;
import com.yaoan.framework.security.config.JwtInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * @author : doujl
 * @className : JwtHelper
 * @description :
 * @date : 2023年12月07日18:05:59
 */
public class JwtHelper {

    public static JwtInfo decode(String token) throws Exception {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        JwtInfo jwtInfo = new JwtInfo();
        if (token.startsWith("Bearer ")) {
            token = token.substring("Bearer ".length());
        }
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        jwtInfo.setUserId(claims.get("userId").asString());
        jwtInfo.setUserName(claims.get("userName").asString());
        jwtInfo.setUserTypeNow(claims.get("userTypeNow").asString());
        jwtInfo.setSystemType(claims.get("systemType").asString());
        return jwtInfo;
    }

    public static void main(String[] args) throws Exception {
//        String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJJRms1Q2JNS3Z6VXJzTnJ0RGtETDlyYzBUWXFpX3EzdFhFZXF3Ml9iR3RzIn0.eyJleHAiOjE2MDkyMjAzNzcsImlhdCI6MTYwOTIyMDMxNywianRpIjoiMzQxZmI0YWItODYzMi00MTkyLWI2OGUtZDM1MTM4ZWRmOTU4IiwiaXNzIjoiaHR0cDovLzE3Mi4zMC4xOTkuMTY5OjM4MDgwL2F1dGgvcmVhbG1zL21hc3RlciIsInN1YiI6IjJiZjlkZmVhLTRhZDgtNDdmMC1hODcxLTkwNTc3ZjQ1YmQzOSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsInNlc3Npb25fc3RhdGUiOiIxNzNlNDUzOS04YjFhLTQ1ZTYtOGZkNC0wMjllZDMzYmU3MGMiLCJhY3IiOiIxIiwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbiJ9.OKQ0EImnM2nJAKE-EBuMKKgLvHItV0wF1aWjBsjb_BcfLOYy305X8I1HTbMCxMGDzUcM1VX_Hdt8qSgntVH0Y25uLP_16biV4s-Em7X2AxFubbXFnVtm5sLxDdcunTpJOcK8nXqnhwP4U4ll09x6YstbDBtd8wUPSFaYCDnyy1mN3BoevOUZ-TMF2k6q3UKvvi_-9lSGD_PWMRV3lrlLc6vn1iOkQVxdVaPTS3YaWlshF110rkEdoEdDyXrSWTdniwRk_fG1-_IttGdmrX58fJjqzfpV9vr3V29C4Dx6cI9tzyoKu67H_i40s5N5nsFibLxcQcWsSzikCn3DYL7aBQ";
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyU3RhdHVzIjoiMSIsImxvZ2luVHlwZSI6MSwidXNlcl9uYW1lIjoi5bGx6KW_5qGG6YeH6YeH6LSt5Y2V5L2N57uP5YqeIiwidXNlclR5cGVJbmZvcyI6W3sidXNlclR5cGVJZCI6IjIiLCJjb21tb25UeXBlIjoiMiIsInN5c3RlbVR5cGUiOiIzIiwic3RhdHVzIjoiMSJ9LHsidXNlclR5cGVJZCI6IjQiLCJjb21tb25UeXBlIjoiNCIsInN5c3RlbVR5cGUiOiIzIiwic3RhdHVzIjoiMSJ9XSwidXNlck5hbWUiOiLlsbHopb_moYbph4fph4fotK3ljZXkvY3nu4_lip4iLCJ1c2VySWQiOiI4OTdmOTUwMDgyOTBhYTU0MDE4MjlmMzZlOTlhMDAwNCIsImNsaWVudF9pZCI6InluZHpodC1jbGllbnQiLCJhdWQiOlsieWFvYW4iLCJncC1wb3J0YWwtY2VudGVyIiwiZ3AtYXV0aC1jZW50ZXIiXSwiaWRlbnRpdHlUeXBlIjoxLCJ1c2VyQWNjb3VudCI6IjIyMDgxNTAxSkIiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwic3lzdGVtVHlwZSI6IjMiLCJ1c2VyVHlwZU5vdyI6IjIiLCJleHAiOjE3MDExNzUzOTMsImp0aSI6ImIzNjVmOTRmLWI5ZTUtNDc1ZC1iMzk3LWIyY2E0MDRhYzdmMiJ9.hOQ6yJT6svaXfXnVtoqoaNMaZR1Gf9LmQx_rlLxH8k9lPmNDIkeB_7hLop03o6XeGv1GhDH8wNq652TOb8lULbpscuPShKeLv1uKkIjD-ixf1L5hoNb7euwtF9X7qRZkYncQIaUwNfteP4gmMNq3s7gJ1kxNIteSUC2JBiuY0RNytCJOLKDNywb300OuIx30sdosNCDXrCaeEzjv3V-EtjCC56a4kJ16C7N-_oUNAbKqXQEoYchgYUJIPndlVyQuygTqQNkddLBPn04mzr2uPkmbAena2A-QxYjKPxdLn_JM_dNvnyPcxb4kaEBeh0j_5-VWgWOlzUAWiRtHemX0tg";
        JwtInfo jwt = JwtHelper.decode(token);
        System.out.println(jwt);
    }

    public static ClientInfo decodePermission(String token) throws JsonProcessingException {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring("Bearer ".length());
        }
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();

        ClientInfo clientInfo = JSONObject.parseObject(claims.get("code").asString(), ClientInfo.class);
        return clientInfo.setExpiresTime(jwt.getExpiresAt());
    }
}
