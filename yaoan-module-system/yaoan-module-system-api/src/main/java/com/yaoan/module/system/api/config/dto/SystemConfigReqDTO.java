package com.yaoan.module.system.api.config.dto;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/23 14:26
 */
@Data
public class SystemConfigReqDTO {

    /**
     * 配置key
     */
    private String cKey;

    /**
     * 流程定义key
     */
    private String proDefKey;

}
