package com.yaoan.module.econtract.controller.admin.relative.vo;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025/3/11 19:41
 */
@Data
public class RelativeSubReqVO {

    private String relativeId;
    /**
     * 提交标识
     * 0=不提交
     * 1=提交
     */
    private Integer isSubmit;

}
