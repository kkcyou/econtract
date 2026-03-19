package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/29 17:32
 */
@Data
public class TermListRespVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 条款在模板里的顺序
     */
    private Integer sort;

    /**
     * 编号
     */
    private String code;

    /**
     * 条款类型
     */
    private String termType;

    /**
     * 条款名称
     */
    private String name;

    /**
     * 条款说明
     */
    private String termComment;

    /**
     * 是否必选
     */
    private Boolean isRequired;

    /**
     * 条款内容
     */
    private byte[] termContent;

    private String title;

    /**
     * 是否展示序号
     */
    private Boolean showSort;

    /**
     * 是否展示名称
     */
    private Boolean showName;

    /**
     * 可否编辑
     */
    private Boolean enableEdit;

    /**
     * 分类
     */
    private String termKind;

    /**
     * 分类
     */
    private String termKindName;

}
