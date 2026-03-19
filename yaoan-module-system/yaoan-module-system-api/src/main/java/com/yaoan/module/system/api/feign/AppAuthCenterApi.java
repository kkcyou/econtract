package com.yaoan.module.system.api.feign;

import com.yaoan.framework.common.pojo.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(url = "${feign.client.config.app.auth-center:https://gcetprv.gcycloud.cn}", name = "AppAuthCenter")
public interface AppAuthCenterApi {

    @GetMapping(value = "/op-gateway/op-applets/app/v1/externalAuth/getUserInfo")
    CommonResult oauthCenterToken(@RequestParam(value = "clientId", required = false) String clientId,
                                  @RequestParam(value = "code", required = false) String code);

}

