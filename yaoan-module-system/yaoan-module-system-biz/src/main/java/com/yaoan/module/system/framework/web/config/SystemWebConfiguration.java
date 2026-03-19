package com.yaoan.module.system.framework.web.config;

import com.yaoan.framework.swagger.config.YudaoSwaggerAutoConfiguration;
import com.yaoan.module.system.util.UtilTools;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * system 模块的 web 组件的 Configuration
 *
 * @author 芋道源码
 */
@Validated
@Data
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SystemWebConfiguration {

    @Value("${validate.license}")
    private String validate;

    @Resource
    private WebApplicationContext webApplicationContext;

    /**
     * system 模块的 API 分组
     */
    @Bean
    @Order(Integer.MIN_VALUE)
    public GroupedOpenApi systemGroupedOpenApi() {
        boolean b = UtilTools.checkLicense(validate);
        log.info("开始校验license------");

        if (!b) {
            log.info("license 校验失败");
            System.exit(SpringApplication.exit(webApplicationContext));
        } else {
            log.info("license 校验成功");
        }
        return YudaoSwaggerAutoConfiguration.buildGroupedOpenApi("system");
    }

}
