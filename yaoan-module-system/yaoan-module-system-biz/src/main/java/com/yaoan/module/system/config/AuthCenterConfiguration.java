package com.yaoan.module.system.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author doujiale
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AuthCenterProperties.class)
public class AuthCenterConfiguration {
}
