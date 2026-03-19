package com.yaoan.module.system.service.auth;

import com.yaoan.module.system.controller.admin.auth.shucai.vo.AuthCodeRespVO;
import com.yaoan.module.system.controller.admin.auth.shucai.vo.AuthTokenCodeReqVO;
import com.yaoan.module.system.controller.admin.auth.vo.*;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;

import javax.validation.Valid;

/**
 * 管理后台的认证 Service 接口
 *
 * 提供用户的登录、登出的能力
 *
 * @author 芋道源码
 */
public interface AdminAuthService {

    /**
     * 验证账号 + 密码。如果通过，则返回用户
     *
     * @param username 账号
     * @param password 密码
     * @return 用户
     */
    AdminUserDO authenticate(String username, String password);

    /**
     * 验证账号 + 密码。如果通过，则返回用户
     *
     * @param username 账号
     * @param password 密码
     * @return 用户
     */
    LoginRespVO authenticate2(String username, String password);

    /**
     * 账号登录YaoAn
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    public AuthLoginRespVO loginYaoan(AuthLoginSimpleReqVO reqVO);

    /**
     * app绑定标识登录
     *
     * @param openId 登录信息
     * @return 登录结果
     */
    public AuthLoginRespVO loginAppOpenId(String openId);

    /**
     * 账号登录
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AuthLoginRespVO login(@Valid AuthLoginReqVO reqVO);

    /**
     * 基于 token 退出登录
     *
     * @param token token
     * @param logType 登出类型
     */
    void logout(String token, Integer logType);

    /**
     * 短信验证码发送
     *
     * @param reqVO 发送请求
     */
    void sendSmsCode(AuthSmsSendReqVO reqVO);
    Boolean useSmsCode(AuthSmsLoginReqVO reqVO);

    /**
     * 短信登录
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AuthLoginRespVO smsLogin(AuthSmsLoginReqVO reqVO) ;
    Boolean useSms(AuthSmsLoginReqVO reqVO) ;

    /**
     * 社交快捷登录，使用 code 授权码
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AuthLoginRespVO socialLogin(@Valid AuthSocialLoginReqVO reqVO);

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 登录结果
     */
    AuthLoginRespVO refreshToken(String refreshToken);

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 登录结果
     */
    AuthLoginRespVO refreshExtendToken(String refreshToken);

    /**
     * 获取许可
     * @param reqVO request
     * @return response
     */
    AuthTokenRespVO authToken(AuthTokenSimpleReqVO reqVO);

    AuthCodeRespVO authCodeToken(AuthTokenCodeReqVO reqVO) throws Exception;

    AuthLoginRespVO loginMock(AuthTokenCodeReqVO reqVO) throws Exception;
}
