package com.yaoan.module.system.service.auth;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.system.api.feign.AppAuthCenterApi;
import com.yaoan.module.system.api.oauth2.OAuth2TokenApi;
import com.yaoan.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.controller.admin.auth.vo.AuthAppClientReqVO;
import com.yaoan.module.system.controller.admin.auth.vo.AuthAppUserRespVO;
import com.yaoan.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.mysql.user.AdminUserMapper;
import com.yaoan.module.system.service.auth.utils.Sm4Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.concurrent.atomic.AtomicReference;

import static com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants.ERROR_PERMISSION_CODE;
import static com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants.UNAUTHORIZED;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;

/**
 * 管理后台的认证 Service 接口
 *
 * 提供用户的登录、登出的能力
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class AppAuthServiceImpl implements AppAuthService{

    @Resource
    private AppAuthCenterApi appAuthCenterApi;
    @Resource
    private AdminUserMapper userMapper;
    @Resource
    private AdminAuthService authService;

    @Value("${justauth.type.GCET_APP.secret_key:42503218C1C2954FFD370CE4205B1B62}")
    private String appSm4Key;

    @Value("${justauth.type.GCET_APP.client-id:761DDCD206DD420DA97F4B1953DF4237}")
    private String clientId;
    @Override
    public CommonResult getUserInfoByApp(AuthAppClientReqVO authAppClientRespVO) {
        String openId = authAppClientRespVO.getOpenId();
        if (ObjectUtil.isNotEmpty(openId)){
            AtomicReference<AdminUserDO> atomic = new AtomicReference<>();
            DataPermissionUtils.executeIgnore(() -> {
                TenantUtils.executeIgnore(() -> {
                    AdminUserDO userByAppOpenId = userMapper.selectByAppOpenId(openId);
                    atomic.set(userByAppOpenId);
                });
            });
            if (ObjectUtil.isEmpty(atomic.get())){
                return new CommonResult().setCode(-1).setData(openId).setMsg("未绑定用户，请先登录");
            } else {
                AuthLoginRespVO authLoginRespVO = authService.loginAppOpenId(openId);
                return new CommonResult().setCode(0).setData(authLoginRespVO).setMsg("登录成功");
            }
        }

        if (ObjectUtil.isEmpty(authAppClientRespVO.getCode())){
            throw exception(DIY_ERROR, "确少参数传递");
        }

        CommonResult result = appAuthCenterApi.oauthCenterToken(clientId, authAppClientRespVO.getCode());
        String msg = result.getMsg();
        Integer code = result.getCode();
        Object data = result.getData();
        if (200 == code){
            try {
                String dataStr = Sm4Utils.decryptEcb(appSm4Key, data.toString(), "UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(dataStr);
                AuthAppUserRespVO authAppUserRespVO = jsonObject.toJavaObject(AuthAppUserRespVO.class);
                String newOpenId = authAppUserRespVO.getUser().getOpenId(); // 用户身份标识
                AtomicReference<AdminUserDO> atomic = new AtomicReference<>();

                DataPermissionUtils.executeIgnore(() -> {
                    TenantUtils.executeIgnore(() -> {
                        AdminUserDO userByAppOpenId = userMapper.selectByAppOpenId(newOpenId);
                        atomic.set(userByAppOpenId);
                    });
                });
                if (ObjectUtil.isEmpty(atomic.get())){
                    return new CommonResult().setCode(-1).setData(newOpenId).setMsg("未绑定用户，请先登录");
                } else {
                    AuthLoginRespVO authLoginRespVO = authService.loginAppOpenId(newOpenId);
                    return new CommonResult().setCode(0).setData(authLoginRespVO).setMsg("登录成功");
                }

            } catch (Exception e) {
                throw exception(DIY_ERROR, e.getMessage());
            }
        } else{
            throw exception(DIY_ERROR, msg);
        }
    }

    @Resource
    private OAuth2TokenApi oauth2TokenApi;
    @Override
    public ResponseEntity<CommonResult<Boolean>> checkFileAuth(HttpServletRequest request) {
        // 从请求头获取 Token
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>( CommonResult.error(UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }
            String token = authHeader.substring(7); // 截取 "Bearer " 后的 Token

            // 看看会话类型中有无该token，有则授权通过，无则继续判断token
            if (oauth2TokenApi.validateToken(token)) {
                return new ResponseEntity<>( CommonResult.success(true), HttpStatus.OK);
            }
            // 校验令牌
            Integer userType = WebFrameworkUtils.getLoginUserType(request);
            OAuth2AccessTokenCheckRespDTO accessToken = oauth2TokenApi.checkAccessToken(token);
            if (accessToken == null) {
                return new ResponseEntity<>( CommonResult.error(UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }
            // 用户类型不匹配，无权限
            if (ObjectUtil.notEqual(accessToken.getUserType(), userType)) {
                return new ResponseEntity<>( CommonResult.error(UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }
            AdminUserRespDTO adminUserRespDTO = oauth2TokenApi.getUserInfoByUserId(accessToken.getUserId());
            if (adminUserRespDTO==null) {
                return new ResponseEntity<>( CommonResult.error(UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>( CommonResult.success(true), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>( CommonResult.error(UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
    }
}
