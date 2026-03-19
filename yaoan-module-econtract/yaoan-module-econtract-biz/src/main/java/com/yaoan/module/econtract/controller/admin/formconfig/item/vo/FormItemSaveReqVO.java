package com.yaoan.module.econtract.controller.admin.formconfig.item.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yaoan.module.econtract.enums.formconfig.ItemSourceEnums;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/20 17:42
 */
@Data
public class FormItemSaveReqVO {
    /**
     * id
     */
    private String id;
    /**
     * 表项编号
     */
    @TableField("code")
    private String code;

    /**
     * 表项名称
     */
    @TableField("name")
    private String name;

    /**
     * 对应字段(对应数据库里的字段名称)
     */
    @TableField("db_name")
    private String dbName;


    /**
     * 表单id
     */
    @TableField("form_id")
    private String formId;

    /**
     * 表项分类
     */
    @TableField("item_category")
    private String itemCategory;

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
     * 备注
     */
    @TableField("remark")
    private String remark;


    /**
     * 表项来源
     * {@link ItemSourceEnums}
     */
    @TableField("item_source")
    private String itemSource;

    /**
     * 排序
     */
    private Integer sort;
}
