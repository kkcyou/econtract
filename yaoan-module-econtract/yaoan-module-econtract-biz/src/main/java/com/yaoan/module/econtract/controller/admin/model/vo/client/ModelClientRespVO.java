package com.yaoan.module.econtract.controller.admin.model.vo.client;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/1 17:46
 */
@Data
public class ModelClientRespVO {


    /**
     * id
     */
    private Long id;

    /**
     * 平台标识
     */
    private String platformFlag;

    /**
     * 平台名称
     */
    private String platformName;
}
