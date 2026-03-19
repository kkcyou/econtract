package com.yaoan.module.econtract.dal.dataobject.annotation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 审批批准
 * @author: Pele
 * @date: 2024/3/25 11:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_annotation")
public class AnnotationDO extends DeptBaseDO {

    private static final long serialVersionUID = -3976907828400461392L;

    /**
     * 主键（存commentId）
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 审批节点
     */
    @TableField("approve_index")
    private Integer approveIndex;

    /**
     * 业务id
     */
    @TableField("business_id")
    private String businessId;

    /**
     * 业务类型
     */
    @TableField("business_type")
    private String businessType;

    /**
     * 解决状态（0未解决 1已解决）
     */
    @TableField("status")
    private Boolean status;

    /**
     * 批注在文本中的位置
     */
    @TableField("location")
    private String location;

    /**
     * 位置范围
     */
    @TableField("annotation_range")
    private String annotationRange;

    /**
     * 文件id
     */
    @TableField("file_id")
    private Long fileId;

    /**
     * 所选定的业务文本
     */
    @TableField("selected_business_text")
    private String selectedBusinessText;

    /**
     * 批注内容
     */
    @TableField("content")
    private String content;

    /**
     * 申请人id
     */
    @TableField("submitter")
    private Long submitter;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}
