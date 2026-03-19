package com.yaoan.module.econtract.controller.admin.annotation.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/25 11:57
 */
@Data
public class AnnotationSaveUpdateReqVO {
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 业务id
     */
    @TableId("business_id")
    private String businessId;

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

    private String range;

    /**
     * 文件id
     */
    @TableField("file_id")
    private Long fileId;

    /**
     * 批注内容
     */
    @TableField("content")
    @NotNull()
    private String content;

    /**
     * 业务标识
     */
    @TableField("business_type")
    private String businessType;

    /**
     * 所选定的业务文本
     */
    @TableField("selected_business_text")
    private String selectedBusinessText;

    /**
     * 下一个审批节点的审批人的id集合
     */
    @TableField("next_approver_ids")
    private String nextApproverIds;

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
