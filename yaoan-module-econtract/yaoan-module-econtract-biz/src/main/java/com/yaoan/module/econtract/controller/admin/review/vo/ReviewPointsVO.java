package com.yaoan.module.econtract.controller.admin.review.vo;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 审查清单和审查点中间表(EcmsReviewPoints)实体类
 *
 * @author wsh
 * @since 2024-03-25 11:36:13
 */
@Data
public class ReviewPointsVO  {

    /**
     * 审查点id
     */
    @NotNull(message = "审查点id不能为空")
    private String pointsId;

    /**
     * 审查点名称
     */
    private String pointsName;

    /**
     * 风险等级，0高风险，1中风险，2低风险
     */
    private Integer grade;
    /**
     * 风险提示，0系统推荐、1自定义
     */
    private Integer prompt;
    /**
     * 风险提示语
     */
    private String riskPrompt;


}

