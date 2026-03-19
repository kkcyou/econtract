package com.yaoan.module.system.framework.common;

import com.yaoan.module.system.framework.core.event.CompanyInitializeEventPublisher;
import com.yaoan.module.system.framework.core.event.SupplyCompanyInitializeEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用的 Configuration 配置类
 */
@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {

    @Bean
    public CompanyInitializeEventPublisher companyInitializeEventPublisher(ApplicationEventPublisher publisher) {
        return new CompanyInitializeEventPublisher(publisher);
    }
    @Bean
    public SupplyCompanyInitializeEventPublisher supplyCompanyInitializeEventPublisher(ApplicationEventPublisher publisher) {
        return new SupplyCompanyInitializeEventPublisher(publisher);
    }
}
