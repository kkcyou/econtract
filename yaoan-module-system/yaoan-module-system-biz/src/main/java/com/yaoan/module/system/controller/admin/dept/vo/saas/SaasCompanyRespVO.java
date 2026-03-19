package com.yaoan.module.system.controller.admin.dept.vo.saas;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-28 15:19
 */
@Data
public class SaasCompanyRespVO {
    /**
     * 公司id
     */
    private Long companyId;

    private String companyName;

    /**
     * 相对方id
     */
    private String relativeId;

    /**
     * 实名情况
     */
    private String realNameStr;

    /**
     * 是否是当前空间
     */
    private Integer currentFlag;

}
