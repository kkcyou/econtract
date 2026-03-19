package com.yaoan.module.econtract.framework.web.config;

import com.yaoan.framework.swagger.config.YudaoSwaggerAutoConfiguration;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * infra 模块的 web 组件的 Configuration
 *
 * @author 芋道源码
 */
@Configuration(proxyBeanMethods = false)
public class EContractWebConfiguration {

    /**
     * infra 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi eContractGroupedOpenApi() {
        return YudaoSwaggerAutoConfiguration.buildGroupedOpenApi("econtract");
    }

}
