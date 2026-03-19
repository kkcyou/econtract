package com.yaoan.module.system.controller.admin.oauth2;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.pojo.TokenResult;
import com.yaoan.framework.security.config.ClientInfo;
import com.yaoan.framework.security.core.util.JwtHelper;
import com.yaoan.module.system.controller.admin.oauth2.vo.client.*;
import com.yaoan.module.system.controller.admin.oauth2.vo.token.OAuth2AccessJWTRespVO;
import com.yaoan.module.system.convert.auth.OAuth2ClientConvert;
import com.yaoan.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.service.oauth2.JwtService;
import com.yaoan.module.system.service.oauth2.OAuth2ClientService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import static com.yaoan.framework.common.pojo.CommonResult.success;
/**
 * 管理后台 - OAuth2 客户端
 */
@Tag(name = "管理后台 - OAuth2 客户端")
@RestController
@RequestMapping("/system/oauth2-client")
@Validated
public class OAuth2ClientController {

    @Resource
    private OAuth2ClientService oAuth2ClientService;

    @Resource
    private JwtService jwtService;

    @PostMapping("/create")
    @Operation(summary = "创建 OAuth2 客户端")
    @PreAuthorize("@ss.hasPermission('system:oauth2-client:create')")
    public CommonResult<Long> createOAuth2Client(@Valid @RequestBody OAuth2ClientCreateReqVO createReqVO) {
        return success(oAuth2ClientService.createOAuth2Client(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新 OAuth2 客户端")
    @PreAuthorize("@ss.hasPermission('system:oauth2-client:update')")
    public CommonResult<Boolean> updateOAuth2Client(@Valid @RequestBody OAuth2ClientUpdateReqVO updateReqVO) {
        oAuth2ClientService.updateOAuth2Client(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除 OAuth2 客户端")
    @Parameter(name = "id", description = "编号", required = true)
//    @PreAuthorize("@ss.hasPermission('system:oauth2-client:delete')")
    public CommonResult<Boolean> deleteOAuth2Client(@RequestParam("id") Long id) {
        oAuth2ClientService.deleteOAuth2Client(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得 OAuth2 客户端")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('system:oauth2-client:query')")
    public CommonResult<OAuth2ClientRespVO> getOAuth2Client(@RequestParam("id") Long id) {
        OAuth2ClientDO oAuth2Client = oAuth2ClientService.getOAuth2Client(id);
        return success(OAuth2ClientConvert.INSTANCE.convert(oAuth2Client));
    }

    @GetMapping("/getCode")
    @Operation(summary = "获得 OAuth2 客户端 code")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<String> getAuth2ClientCode(@RequestParam("id") Long id) throws Exception {
        OAuth2ClientDO oAuth2Client = oAuth2ClientService.getOAuth2Client(id);

        String userJson = JSON.toJSONString(oAuth2Client);//序列化user
        JwtBuilder jwtBuilder = Jwts.builder(); //获得JWT构造器
        Map<String, Object> map = new Hashtable<>();
        map.put("code", userJson);
        String token = jwtBuilder.setSubject("hello") //设置用户数据
                .setIssuedAt(new Date()) //设置jwt生成时间
                .setId(oAuth2Client.getId().toString()) //设置id为token id
                .setClaims(map) //通过map传值
                .setExpiration(new DateTime(oAuth2Client.getExpiresTime())) //设置token有效期
                .signWith(SignatureAlgorithm.HS256, "YaoAnTech") //设置token加密方式和密码
                .compact(); //生成token字符串

        System.out.println("token:" + token);
        ClientInfo client = JwtHelper.decodePermission(token);
        return success(token);
    }

    @PermitAll
    @PostMapping("/access/code")
    @Operation(summary = "根据客户端账号、获得 OAuth2 客户端 code")
    public TokenResult postAccessToken(@Valid @RequestBody OAuth2AccessCodeReqVO reqVO) throws Exception {
//        OAuth2ClientDO oAuth2Client = oAuth2ClientService.getOAuth2Client(reqVO.getClientId(),reqVO.getSecret());
//
//        String userJson = JSON.toJSONString(oAuth2Client);//序列化user
//        JwtBuilder jwtBuilder = Jwts.builder(); //获得JWT构造器
//        Map<String, Object> map = new Hashtable<>();
//        map.put("code", userJson);
//        String token = jwtBuilder.setSubject("hello") //设置用户数据
//                .setIssuedAt(new Date()) //设置jwt生成时间
//                .setId(oAuth2Client.getId().toString()) //设置id为token id
//                .setClaims(map) //通过map传值
//                .setExpiration(DateUtil.offsetMillisecond(new Date(), oAuth2Client.getAccessTokenValiditySeconds() * 100000)) //设置token有效期
//                .signWith(SignatureAlgorithm.HS256, "YaoAnTech") //设置token加密方式和密码
//                .compact(); //生成token字符串
//        
//        System.out.println("token:" + token);
//        return success(token);
        OAuth2ClientDO oAuth2Client = oAuth2ClientService.getOAuth2Client(reqVO.getClientId(),reqVO.getSecret());

        String userJson = JSON.toJSONString(oAuth2Client);//序列化user
        JwtBuilder jwtBuilder = Jwts.builder(); //获得JWT构造器
        Map<String, Object> map = new Hashtable<>();
        map.put("code", userJson);
        String token = jwtBuilder.setSubject("hello") //设置用户数据
                .setIssuedAt(new Date()) //设置jwt生成时间
                .setId(oAuth2Client.getId().toString()) //设置id为token id
                .setClaims(map) //通过map传值
                .setExpiration(DateUtil.offsetMillisecond(new Date(), oAuth2Client.getAccessTokenValiditySeconds() * 100000)) //设置token有效期
                .signWith(SignatureAlgorithm.HS256, "YaoAnTech") //设置token加密方式和密码
                .compact(); //生成token字符串

        TokenResult tokenResult = new TokenResult().setAccess_token(token).setToken_type("bearer").setScope("read write").setExpires_in("35999");
//        Claims claims = Jwts.parser().setSigningKey("YaoAnTech").parseClaimsJws(token).getBody();
//        System.out.println("claims:" + claims);
        return tokenResult;
    }

    public static void main(String[] args) {
        JwtBuilder jwtBuilder = Jwts.builder(); //获得JWT构造器
        Map<String, Object> map = new Hashtable<>();
        map.put("sub", "anon_hash_abc123");
        map.put("bizId", "BIZ-20250908-001");
        map.put("tenantId", "TEN-001");
        map.put("region", "440300");
        map.put("action", "template:view");
        map.put("page", "template-library");
        map.put("keyId", "key-2025-Q3");
        map.put("scope", Arrays.asList("TPL-PO","TPL-NDA"));
        map.put("reviewScope", Arrays.asList("CLAUSE-001","CLAUSE-002"));
        String token = jwtBuilder.setSubject("hello") //设置用户数据
                .setIssuedAt(new Date()) //设置jwt生成时间
                .setId("uuid-xxxx") //设置id为token id
                .setClaims(map) //通过map传值
                .setIssuedAt(new Date()) // 签发时间
                .setIssuer("BIZ-SYS-A")
                .setAudience("econtract")
                .setExpiration(DateUtil.offsetMillisecond(new Date(), 3600 * 1000)) //设置token有效期
                .signWith(SignatureAlgorithm.HS256, "YaoAnTech") //设置token加密方式和密码
                .compact(); //生成token字符串
        System.out.println(token);
        //String token = "eyJhbGciOiJIUzI1NiJ9.eyJjb2RlIjoie1wiYWNjZXNzVG9rZW5WYWxpZGl0eVNlY29uZHNcIjowLFwiY2xpZW50SWRcIjpcInByb2R1Y3QtY29udHJhY3Qtc3luY1wiLFwiY3JlYXRlVGltZVwiOlwiMjAyNC0xMC0yOVQxMTowMjo1OVwiLFwiY3JlYXRvclwiOlwiMVwiLFwiZGVsZXRlZFwiOmZhbHNlLFwiZGVzY3JpcHRpb25cIjpcIjFcIixcImV4cGlyZXNUaW1lXCI6XCIyMDI5LTEwLTMxVDAwOjAwOjAwXCIsXCJpZFwiOjU2LFwibG9nb1wiOlwiMVwiLFwibmFtZVwiOlwi6buR6b6Z5rGfXCIsXCJyZWZyZXNoVG9rZW5WYWxpZGl0eVNlY29uZHNcIjowLFwic2VjcmV0XCI6XCJwcm9kdWN0LXNlY3JldFwiLFwic3RhdHVzXCI6MCxcInVwZGF0ZVRpbWVcIjpcIjIwMjQtMTAtMjlUMDM6MTM6MDVcIixcInVwZGF0ZXJcIjpcIjFcIn0iLCJleHAiOjE3MzAxODE5NTR9.0Ut22NTeyV2GpRuMooenyigX3ejpH0_GOreoLSy0THg";
        Claims claims = Jwts.parser().setSigningKey("YaoAnTech").parseClaimsJws(token).getBody();
        System.out.println("claims:" + claims);
        System.out.println("claims:" + claims.get("iss"));
        System.out.println("claims:" + claims.getIssuer());
    }
    @GetMapping("/page")
    @Operation(summary = "获得OAuth2 客户端分页")
    @PreAuthorize("@ss.hasPermission('system:oauth2-client:query')")
    public CommonResult<PageResult<OAuth2ClientRespVO>> getOAuth2ClientPage(@Valid OAuth2ClientPageReqVO pageVO) {
        PageResult<OAuth2ClientDO> pageResult = oAuth2ClientService.getOAuth2ClientPage(pageVO);
        return success(OAuth2ClientConvert.INSTANCE.convertPage(pageResult));
    }


    //=====2024-03-05 新增=====
    @PostMapping("/createV2")
    @Operation(summary = "创建客户端V2")
//    @PreAuthorize("@ss.hasPermission('system:oauth2-client:create')")
    public CommonResult<Long> createOAuth2ClientV2(@Valid @RequestBody OAuth2ClientCreateReqVO createReqVO) {
        return success(oAuth2ClientService.createOAuth2ClientV2(createReqVO));
    }
    @PutMapping("/updateV2")
    @Operation(summary = "更新客户端V2")
//    @PreAuthorize("@ss.hasPermission('system:oauth2-client:update')")
    public CommonResult<Boolean> updateOAuth2ClientV2(@Valid @RequestBody OAuth2ClientUpdateReqVO updateReqVO) {
        oAuth2ClientService.updateOAuth2ClientV2(updateReqVO);
        return success(true);
    }
    @PostMapping("/pageV2")
    @Operation(summary = "获得客户端分页V2")
//    @PreAuthorize("@ss.hasPermission('system:oauth2-client:query')")
    public CommonResult<PageResult<OAuth2ClientV2RespVO>> getOAuth2ClientPageV2(@RequestBody @Valid OAuth2ClientPageReqVO pageVO) {
        PageResult<OAuth2ClientV2RespVO> pageResult = oAuth2ClientService.getOAuth2ClientPageV2(pageVO);
        return success(pageResult);
    }
    @GetMapping("/refresh/{id}")
    @Operation(summary = "刷新secret")
    public CommonResult<Boolean> refreshSecret(@PathVariable Long id) {
        oAuth2ClientService.refreshSecret(id);
        return success(true);
    }
    @GetMapping("/getV2")
    @Operation(summary = "获得 OAuth2 客户端v2")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('system:oauth2-client:query')")
    public CommonResult<OAuth2ClientRespVO> getOAuth2ClientV2(@RequestParam("id") Long id) {
        OAuth2ClientRespVO oAuth2Client = oAuth2ClientService.getOAuth2ClientV2(id);
        return success(oAuth2Client);
    }

    /**
     * 解析jwt密钥返回一个sessionId
     */
    @PostMapping("/access/jwt/{token}")
    @PermitAll
    @Operation(summary = "验证token获取临时会话id")
    public CommonResult<OAuth2AccessJWTRespVO> postAccessToken(@PathVariable String token) throws Exception {
        return success(jwtService.validateJwtToken(token));
    }

    @GetMapping("/isSessionTimeOut/{sessionId}")
    @Operation(summary = "校验会话是否过期")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PermitAll
    public CommonResult<Boolean> isSessionTimeOut(@PathVariable String sessionId) {
        return success(jwtService.validateToken(sessionId));
    }
}
