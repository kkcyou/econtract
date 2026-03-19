package com.yaoan.module.econtract.framework.sppgpt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "sppgpt.client")
@Validated
@Data
public class SppGPTProperties {

    /**
     * appId
     */
    @NotNull(message = "appId不能为空")
    private String appId;
    /**
     * secret
     */
    @NotNull(message = "secret不能为空")
    private String secret;


}
