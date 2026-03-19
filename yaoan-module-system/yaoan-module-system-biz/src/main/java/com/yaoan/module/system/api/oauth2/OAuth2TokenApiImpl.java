package com.yaoan.module.system.api.oauth2;

import com.yaoan.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import com.yaoan.module.system.api.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import com.yaoan.module.system.api.oauth2.dto.OAuth2AccessTokenRespDTO;
import com.yaoan.module.system.api.oauth2.dto.OAuth2ClientCheckRespDTO;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.convert.auth.OAuth2ClientConvert;
import com.yaoan.module.system.convert.auth.OAuth2TokenConvert;
import com.yaoan.module.system.convert.user.UserConvert;
import com.yaoan.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.service.oauth2.JwtService;
import com.yaoan.module.system.service.oauth2.OAuth2ClientServiceImpl;
import com.yaoan.module.system.service.oauth2.OAuth2TokenService;
import com.yaoan.module.system.service.user.AdminUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * OAuth2.0 Token API 实现类
 *
 * @author 芋道源码
 */
@Service
public class OAuth2TokenApiImpl implements OAuth2TokenApi {

    @Resource
    private OAuth2TokenService oauth2TokenService;
    @Resource
    private OAuth2ClientServiceImpl oauth2ClientService;
    @Resource
    private AdminUserService adminUserService;
    @Resource
    private JwtService jwtService;

    @Override
    public OAuth2AccessTokenRespDTO createAccessToken(OAuth2AccessTokenCreateReqDTO reqDTO) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(
                reqDTO.getUserId(), reqDTO.getUserType(), reqDTO.getClientId(), reqDTO.getCompanyId(), reqDTO.getScopes());
        return OAuth2TokenConvert.INSTANCE.convert2(accessTokenDO);
    }

    @Override
    public OAuth2AccessTokenCheckRespDTO checkAccessToken(String accessToken) {
        return OAuth2TokenConvert.INSTANCE.convert(oauth2TokenService.checkAccessToken(accessToken));
    }

    @Override
    public OAuth2ClientCheckRespDTO getClientInfo(Long clientId) {
        return OAuth2ClientConvert.INSTANCE.convert2DTO(oauth2ClientService.getOAuth2Client(clientId));
    }

    @Override
    public OAuth2AccessTokenRespDTO removeAccessToken(String accessToken) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.removeAccessToken(accessToken);
        return OAuth2TokenConvert.INSTANCE.convert2(accessTokenDO);
    }

    @Override
    public OAuth2AccessTokenRespDTO refreshAccessToken(String refreshToken, String clientId) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.refreshAccessToken(refreshToken, clientId);
        return OAuth2TokenConvert.INSTANCE.convert2(accessTokenDO);
    }
    @Override
    public List<AdminUserRespDTO> getUsersByPlatformUserId(String userId) {
        List<AdminUserDO> users = adminUserService.getUsersByPlatformUserId(userId);
        return UserConvert.INSTANCE.convertList4(users);
    }
    @Override
    public AdminUserRespDTO getUserInfoByUserId(Long userId) {
        AdminUserDO userDO = adminUserService.getUser(userId);
        return UserConvert.INSTANCE.convert4(userDO);
    }

    @Override
    public Boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }
}
