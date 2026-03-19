package com.yaoan.module.econtract.controller.admin.formconfig.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/20 9:52
 */
@Data
public class FormConfigSingleRespVO {
    /**
     * 业务id
     */
    private String businessId;

    /**
     * 业务名称
     */
    private String name;

    /**
     * 表单集合
     */
    private List<FormRespVO> formReqVOS;
}
