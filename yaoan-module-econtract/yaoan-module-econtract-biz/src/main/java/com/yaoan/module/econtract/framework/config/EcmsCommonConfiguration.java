package com.yaoan.module.econtract.framework.config;

import com.yaoan.module.econtract.framework.core.event.EcmsContractResultEventPublisher;
import com.yaoan.module.econtract.framework.core.event.version.FileVersionEventPublisher;
import com.yaoan.module.econtract.framework.core.event.warning.EcmsWarningEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用的 Configuration 配置类，提供给 Contract
 * @author doujiale
 */
@Configuration(proxyBeanMethods = false)
public class EcmsCommonConfiguration {

    @Bean
    public EcmsContractResultEventPublisher ecmsContractResultEventPublisher(ApplicationEventPublisher publisher) {
        return new EcmsContractResultEventPublisher(publisher);
    }
    @Bean
    public FileVersionEventPublisher fileVersionEventPublisher(ApplicationEventPublisher publisher) {
        return new FileVersionEventPublisher(publisher);
    }

    @Bean
    public EcmsWarningEventPublisher ecmsWarningEventPublisher(ApplicationEventPublisher publisher) {
        return new EcmsWarningEventPublisher(publisher);
    }

}
