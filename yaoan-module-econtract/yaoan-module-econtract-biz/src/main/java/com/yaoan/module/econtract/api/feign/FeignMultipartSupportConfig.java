package com.yaoan.module.econtract.api.feign;

import feign.Logger;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
/**
 * @description: 解决feign文件传递问题的配置类
 * @author: Pele
 * @date: 2025/1/7 13:36
 */
@Configuration
public class FeignMultipartSupportConfig {
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;
    @Bean
    @Primary
    @Scope("prototype")
    public Encoder multipartFormEncoder(){
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
    @Bean
    public feign.Logger.Level multipartLoggerLevel(){
        return feign.Logger.Level.FULL;
    }

}
