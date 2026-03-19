package com.yaoan.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * 项目的启动类
 *
 * @author doujl
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${yudao.info.base-package}
@EnableFeignClients(basePackages = {"com.yaoan.module.econtract.api", "com.yaoan.module.system.api"})
//@EnableApolloConfig
@SpringBootApplication(scanBasePackages = {"${yudao.info.base-package}.server", "${yudao.info.base-package}.module"})
@EnableElasticsearchRepositories(basePackages = {"${yudao.info.base-package}.server", "${yudao.info.base-package}.module"})
public class YudaoServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(YudaoServerApplication.class, args);
    }

}
