package com.yaoan.module.econtract.controller.admin.formconfig.form.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.module.econtract.enums.formconfig.ItemSourceEnums;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/21 11:05
 */
@Data
public class FormShowItemRespVO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 表项名称
     */
    @TableField("name")
    private String name;

    /**
     * 表项类型
     */
    @TableField("item_type")
    private String itemType;

    /**
     * 状态 0=停用 1=启用
     */
    @TableField("status")
    private Boolean status;

    /**
     * 是否显示 0=隐藏 1=显示
     */
    @TableField("if_show")
    private Boolean ifShow;

    /**
     * 正则表达式
     */
    @TableField("regular_expression")
    private String regularExpression;

    /**
     * 是否必填 0=否 1=是
     */
    @TableField("if_required")
    private Boolean ifRequired;

    /**
     * 长度
     */
    @TableField("length")
    private Long length;

    /**
     * 宽度
     */
    @TableField("width")
    private Long width;

    /**
     * 占位提示
     */
    @TableField("placeholder")
    private String placeholder;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
}
