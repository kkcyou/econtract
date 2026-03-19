package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import com.yaoan.module.econtract.enums.term.TermTypeEnums;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/8 16:43
 */
@Data
public class TermOneRespVO {
    private String id;

    /**
     * 条款编码
     */
    private String code;

    private String name;

    private Integer sort;

    /**
     * 条款说明
     */
    private String termComment;
    /**
     * 是否必选
     */
    private Boolean isRequired;

    /**
     * 是否展示序号
     */
    private Boolean showSort;

    /**
     * 是否展示名称
     */
    private Boolean showName;

    /**
     * 条款类型(head合同封页，com合同条款，end合同结尾)
     * {@link TermTypeEnums}
     */
    private String termType;
    /**
     * 条款内容
     */
    private String termContent;

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
