package com.yaoan.module.econtract.controller.admin.term.vo.tree;

import lombok.Data;

import java.util.List;

/**
 * @description: 条款树形结构 reqVO
 * @author: Pele
 * @date: 2024/1/9 16:01
 */
@Data
public class TermTreeReqVO {

    /**
     * 条款分类id
     */
    private String categoryId;

    /**
     * 条款类型(head合同封页，com合同条款，end合同结尾)
     */
    private String termType;

    /**
     * 条款名称
     */
    private String name;

    private List<String> categoryIdList;
}
