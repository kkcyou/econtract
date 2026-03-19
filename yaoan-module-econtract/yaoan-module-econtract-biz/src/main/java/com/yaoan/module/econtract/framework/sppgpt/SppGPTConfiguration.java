package com.yaoan.module.econtract.framework.sppgpt;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author doujiale
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SppGPTProperties.class)
public class SppGPTConfiguration {
}
