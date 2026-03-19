package com.yaoan.module.system.service.oauth2;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaoan.framework.common.util.number.NumberUtils;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.module.system.controller.admin.oauth2.vo.client.OAuth2JWTReqVO;
import com.yaoan.module.system.controller.admin.oauth2.vo.token.OAuth2AccessJWTRespVO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;

/**
 * System 层 JWT 核心服务：封装 Token 生成、解析、验证逻辑
 */
@Service
public class JwtService {
    // 从配置文件读取 JWT 密钥（建议配置在 yml 中，避免硬编码）
    @Value("${yudao.jwt.secret-key:MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwSEfDRYtuY7jNlJ8ubf9OnWLd3YQZEtDXhXKCY/wkAcGL2+LkG1nCBpwq16emTeq6Sv7nuwElcgkwT81cLYm8+Mu74M5UZfLUIlsc3/TI1TOKRGvzRjtsr1R8xmBXI83xJJU/oV5lP+MSsvjDHyVPizlaDffPltU/gwR7RMUanlW2Tq0Kw2MWkmZSKxUodqtLNfaAkkUdsT9yna4UIgSCmZgrJx+0vUZj5lErmNYqyEzAHkZtdKFVEPfpUfElS4JNT+82SxHEE2V86xlWb5F/Z4MFz7DF03qK+W0HQaW712/09O8BkHr1n0NVcrvOIW+rGuqf4Lp1yzIAG1f/EQJdQIDAQAB}")
    private String secretKey;

    // 从配置文件读取 Token 过期时间（如 2 小时，单位：毫秒）
    @Value("${yudao.jwt.expire-time: 14400}")
    private Long expireTime;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 2. 解析 Token 并获取 Claims（含用户信息）
     *
     * @param token 待解析的 Token
     * @return JWT 载荷（Claims）
     * @throws ExpiredJwtException Token 过期
     * @throws Exception           Token 篡改、格式错误等
     */
    public Claims parseToken(String token) throws Exception {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 3. 验证 Token 有效性（含过期、关联用户存在性）
     *
     * @param token 待验证的 Token
     * @return 有效返回 true，无效返回 false
     */
    public Boolean validateToken(String token) {
        try {
            if (!redisUtils.hasKey("contract:session:"+token)) {
               return false;
            }
            // 调用 system 层用户服务查询用户（需注入 UserService）
            return true;
        } catch (ExpiredJwtException e) {
            // Token 过期
            return false;
        } catch (Exception e) {
            // Token 篡改、格式错误、无关联用户等
            return false;
        }
    }


    public OAuth2AccessJWTRespVO validateJwtToken(String token) {
        OAuth2AccessJWTRespVO oAuth2AccessJWTRespVO = new OAuth2AccessJWTRespVO();
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] publicKeyBytes = Base64.getDecoder().decode(secretKey);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            RSAPublicKey PUBLIC_KEY =  (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
            JWTVerifier require = JWT.require(Algorithm.RSA256(PUBLIC_KEY,null)).build();

            DecodedJWT decode;
            try {
                decode = require.verify(token);
            } catch (Exception e) {
                throw exception(DIY_ERROR, "token验证失败:"+e.getMessage());
            }
            Map<String, Claim> claims = decode.getClaims();
            claims.forEach((k, v) -> {
                if (Objects.equals(k, "jti")){
                    if (redisUtils.hasKey("contract:nonce:"+v.asString())) {
                        throw exception(DIY_ERROR, "token验证失败, jwt存在重放校验风险异常");
                    } else {
                        redisUtils.set("contract:nonce:"+v.asString(), true, expireTime);
                    }
                }
                if (Objects.equals(k, "exp")){
                    oAuth2AccessJWTRespVO.setExpiresIn(v.asLong());
                }
                if (Objects.equals(k, "page")){
                    oAuth2AccessJWTRespVO.setRedirect(v.asString());
                }
                if (Objects.equals(k, "model")){
                    oAuth2AccessJWTRespVO.setModel(v.asString());
                }
                if (Objects.equals(k, "action")){
//                    oAuth2AccessJWTRespVO.setPermissions(v.asList(String.class));
                    oAuth2AccessJWTRespVO.setAction(v.asString());
                }
                System.out.println(k + " ：" + String.valueOf(v));
            });


            Long sessionId = NumberUtils.generate();
            oAuth2AccessJWTRespVO.setSessionId(sessionId.toString());
            OAuth2JWTReqVO convert = convert(claims, OAuth2JWTReqVO.class);

            redisUtils.set("contract:session:"+sessionId, JSON.toJSONString(convert), expireTime);
            // 调用 system 层用户服务查询用户（需注入 UserService）
            return oAuth2AccessJWTRespVO;
        } catch (ExpiredJwtException e) {
            // Token 过期
            throw exception(DIY_ERROR, "token验证失败,已过期");
        } catch (Exception e) {
            // Token 篡改、格式错误、无关联用户等
            throw exception(DIY_ERROR, "token验证失败:"+e.getMessage());
        }
    }
    public static <T> T convert(Map<String, Claim> claims, Class<T> doClass) {
        if (claims == null || doClass == null) {
            return null;
        }

        try {
            // 1. 创建 DO 实例
            T doInstance = doClass.getDeclaredConstructor().newInstance();

            // 2. 获取 DO 的所有字段
            Field[] fields = doClass.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName(); // DO 字段名（如 "userId"）
                Claim claim = claims.get(fieldName); // 根据字段名获取 Claim

                if (claim == null) {
                    continue; // 若 JWT 中无该字段，跳过（不赋值）
                }

                // 3. 获取字段的 Setter 方法（如 setUserId）
                String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method setterMethod = doClass.getMethod(setterMethodName, field.getType());

                // 4. 根据 DO 字段类型，转换 Claim 并调用 Setter 赋值
                Object value = convertClaimToFieldType(claim, field.getType());
                if (value != null) {
                    setterMethod.invoke(doInstance, value);
                }
            }

            return doInstance;

        } catch (Exception e) {
            // 处理反射异常（如无无参构造、Setter 方法不存在、类型转换失败）
            throw new RuntimeException("Claims 转 DO 失败：" + e.getMessage(), e);
        }
    }

    /**
     * 将 Claim 转换为 DO 字段的目标类型
     * @param claim 待转换的 Claim
     * @param targetType DO 字段的类型（如 String.class、Integer.class、Date.class）
     * @return 转换后的值
     */
    private static Object convertClaimToFieldType(Claim claim, Class<?> targetType) {
        if (claim == null || targetType == null) {
            return null;
        }

        try {
            // 根据目标类型调用 Claim 对应的转换方法
            if (targetType == String.class) {
                return claim.asString();
            } else if (targetType == Integer.class || targetType == int.class) {
                return claim.asInt();
            } else if (targetType == Long.class || targetType == long.class) {
                return claim.asLong();
            } else if (targetType == Boolean.class || targetType == boolean.class) {
                return claim.asBoolean();
            } else if (targetType == Date.class) {
                return claim.asDate();
            } else if (targetType == Double.class || targetType == double.class) {
                return claim.asDouble();
            } else {
                // 若为自定义类型（如枚举），需自行扩展转换逻辑
                throw new UnsupportedOperationException("不支持的字段类型：" + targetType.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Claim 类型转换失败（目标类型：" + targetType.getName() + "）：" + e.getMessage(), e);
        }
    }

}