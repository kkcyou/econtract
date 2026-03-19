package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 合同支付计划
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
public class ContractTermDTO implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 条款编码
     */
    private String termCode;

    /**
     * 条款名称
     */
    private String termName;

    /**
     * 条款类型(head合同封页，com合同条款，end合同结尾)
     */
    private String termType;
    /**
     * 条款内容
     */
    private String termContent;
    /**
     * 条款序号
     */
    private Integer termNum;

    /**
     * 是否展示序号
     */
    private Boolean showSort;

    /**
     * 是否必选
     */
    private Boolean isRequired;

    private String title;
    private String name;

    private String termComment;


    /**
     * ---------------------------------------------------------------- 内蒙追加 ----------------------------------------------------------------
     * */

    /**
     * 是否展示名称
     */
    private Boolean showName;
    /**
     * 能否编辑 0能编辑  1不能编辑
     */
    private Integer editable;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 分类
     */
    private String termKind;

    /**
     * 分类
     */
    private String termKindName;

    /**
     * 可否编辑
     */
    private Boolean enableEdit;
}
