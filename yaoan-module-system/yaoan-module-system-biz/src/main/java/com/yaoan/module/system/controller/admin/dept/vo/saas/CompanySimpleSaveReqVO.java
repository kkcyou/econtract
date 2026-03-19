package com.yaoan.module.system.controller.admin.dept.vo.saas;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-25 16:47
 */
@Data
public class CompanySimpleSaveReqVO {


    /**
     * 企业名称
     * */
    private String name;

    /**
     * 社会信用代码
     * */
    private String creditCode;

    /**
     * 用户id
     * */
    private Long userId;

}
