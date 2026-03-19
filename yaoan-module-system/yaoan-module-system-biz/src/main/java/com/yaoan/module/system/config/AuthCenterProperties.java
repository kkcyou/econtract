package com.yaoan.module.system.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@RefreshScope
@Component("authCenterProperties")
@ConfigurationProperties(prefix = "shucai.client")
@Data
public class AuthCenterProperties implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AuthCenterProperties.class);


    private String clientId;

    @NotNull(message = "secretId不能为空")
    private String secretId;

    private String username;

    private String password;

    private String redirectUri;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("AuthCenterProperties initialized - clientId: {}, clientSecret: {}, username: {}, password: {}, redirectUri: {}",
                clientId, secretId, username, password, redirectUri);

    }
}
