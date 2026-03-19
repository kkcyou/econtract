package com.yaoan.module.system.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 监听apollo配置, 实时刷新 @ConfigurationProperties 中的配置
 */
@Slf4j
@Component
public class ApolloRefreshConfig {

    private final AuthCenterProperties authCenterProperties;
    private final RefreshScope refreshScope;

    public ApolloRefreshConfig(
            final AuthCenterProperties authCenterProperties,
            final RefreshScope refreshScope) {
        this.authCenterProperties = authCenterProperties;
        this.refreshScope = refreshScope;
    }

    @ApolloConfigChangeListener(value = "application-local.yaml", interestedKeyPrefixes = {"auth."})
    private void refresh(ConfigChangeEvent changeEvent) {
        log.info("before refresh {}", authCenterProperties.toString());
        refreshScope.refresh("authCenterProperties");
        log.info("after refresh {}", authCenterProperties);
    }

}
