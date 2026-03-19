package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.query;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/22 15:37
 */
@Data
public class ApplyInfoRespVO {
    /**
     * 标题
     */
    private String title;
    /**
     * 申请人id
     */
    private String applicantId;
    /**
     * 申请人名字
     */
    private String applicantName;
    /**
     * 申请部门id
     */
    private String deptId;
    /**
     * 申请部门
     */
    private String deptName;

    /**
     * 申请时间
     */
    private Date applyDate;

}
