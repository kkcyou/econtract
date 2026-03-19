package com.yaoan.module.system.service.auth;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.system.controller.admin.auth.vo.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 管理后台的认证 Service 接口
 *
 * 提供用户的登录、登出的能力
 *
 * @author 芋道源码
 */
public interface AppAuthService {

    CommonResult getUserInfoByApp(AuthAppClientReqVO authAppClientRespVO);

    ResponseEntity<CommonResult<Boolean>> checkFileAuth(HttpServletRequest request);
}
