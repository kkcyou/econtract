package com.yaoan.module.system.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 数采对接服务
 *
 * @author doujiale
 */
@FeignClient(url = "${feign.client.shucai.test.url:https://test.gcycloud.cn}", name = "shucai")
public interface IShuCaiApi {

    @PostMapping(value = "/gateway/gp-auth-center/oauth/token")
    String oauthToken(@RequestParam("grant_type") String grantType,
                      @RequestParam(value = "client_id", required = false) String client_id,
                      @RequestParam(value = "client_secret", required = false) String client_secret,
                      @RequestParam(value = "code", required = false) String code, // 授权码模式
                      @RequestParam(value = "redirect_uri", required = false) String redirectUri);

    @PostMapping(value = "/gateway/gp-auth-center/oauth/token")
    String oauthCenterToken(@RequestParam("grant_type") String grantType,
                            @RequestParam(value = "client_id", required = false) String client_id,
                            @RequestParam(value = "client_secret", required = false) String client_secret,
                            @RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "password", required = false) String password);

    @GetMapping(value = "/gateway/gp-auth-center/rest/v1/user/userInfo")
    String userInfo(@RequestHeader("access_token") String access_token);

    @GetMapping(value = "/gateway/api/oauth/logout")
    String logout(@RequestHeader("access_token") String access_token, @RequestParam("logoutRedirectUrl") String logoutRedirectUrl);

    @GetMapping(value = "/gpbs-supplier/rest/v1/supplier/supplierinfo/bean/{userId}")
    String supplierInfo(@RequestHeader("access_token") String access_token, @PathVariable("userId") String userId);

    @GetMapping(value = "/gpba-agency/rest/v1/remote/agency/query/info/{userId}")
    String agencyInfo(@RequestHeader("access_token") String access_token, @PathVariable("userId") String userId);

    @GetMapping(value = "/gpbp-gpbp/v1/remote/org/{userGuid}")
    String orgInfo(@PathVariable("userGuid") String userGuid);


}
