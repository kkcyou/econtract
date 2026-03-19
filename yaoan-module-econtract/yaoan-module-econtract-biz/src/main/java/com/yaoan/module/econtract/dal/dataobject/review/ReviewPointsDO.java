package com.yaoan.module.econtract.dal.dataobject.review;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;

import java.io.Serializable;

/**
 * 审查清单和审查点中间表(EcmsReviewPoints)实体类
 *
 * @author makejava
 * @since 2024-03-25 11:36:13
 */
@Data
@TableName("ecms_review_points_rel")
public class ReviewPointsDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 866106825503181774L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 审查点id
     */
    private String pointsId;

    /**
     * 审查点名称
     */
    private String pointsName;

    /**
     * 审查清单id
     */
    private String reviewId;
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

