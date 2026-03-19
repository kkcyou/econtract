package com.yaoan.module.system.api.auth;

import com.yaoan.module.system.api.auth.dto.AuthLoginRespDTO;
import com.yaoan.module.system.enums.logger.LoginLogTypeEnum;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-30 21:11
 */
public interface AuthApi {
     AuthLoginRespDTO createTokenAfterLoginSuccess(Long userId, String username, LoginLogTypeEnum logType, int firstLogin, Long companyId);
}
