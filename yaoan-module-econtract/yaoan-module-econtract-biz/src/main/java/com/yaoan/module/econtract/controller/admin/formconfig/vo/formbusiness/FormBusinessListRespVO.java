package com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/19 19:02
 */
@Data
public class FormBusinessListRespVO {
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 状态 0=停用 1=启用
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
