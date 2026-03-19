package com.yaoan.module.econtract.controller.admin.annotation.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/25 11:58
 */
@Data
public class AnnotationListRespVO {

    /**
     * 主键
     */
    @TableId("id")
    private String id;

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
     * 批注内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建者
     */
    private Long creator;

    /**
     * 创建者名称
     */
    private String creatorName;

    /**
     * 创建者名称
     */
    private LocalDateTime createTime;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 创建者角色
     */
    private Long roleId;

    /**
     * 创建者的角色
     */
    private String creatorRoleName;

    /**
     * 创建者的岗位名称
     */
    private String creatorPostName;
}
