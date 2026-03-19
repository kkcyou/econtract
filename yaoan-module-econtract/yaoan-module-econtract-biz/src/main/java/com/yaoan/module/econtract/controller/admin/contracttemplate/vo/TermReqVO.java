package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import lombok.Data;

@Data
public class TermReqVO {
    private String id;

    /**
     * 条款内容
     */
    private String term;

    /**
     * 坐标
     */
    private String position;

    /**
     * 说明
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 条款在该范本里的标题
     */
    private String title;


}
