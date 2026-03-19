package com.yaoan.module.econtract.controller.admin.reviewitembasis.vo;


import lombok.*;


/**
 * 合同审查规则依据 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 * @author wsh
 */
@Data
public class ReviewItemBasisBaseVO {

    /**
     * 依据ID
     */
    private String id;
    /**
     * 依据类型
     */
    private String type;
    /**
     * 依据类型
     */
    private String typeName;
    /**
     * 依据标题
     */
    private String title;
    /**
     * 依据内容
     */
    private String content;
    /**
     * 规则id
     */
    private String reviewId;
}
