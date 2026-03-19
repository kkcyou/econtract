package com.yaoan.module.econtract.controller.admin.formconfig.vo.item;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.enums.formconfig.ItemSourceEnums;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/19 11:16
 */
@Data
public class FormItemReqVO {
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
     * 表项来源
     * {@link ItemSourceEnums}
     */
    @TableField("item_source")
    private String itemSource;

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
     * 占位提示
     */
    @TableField("placeholder")
    private String placeholder;

    /**
     * 内容
     */
    @TableField("item_content")
    private String itemContent;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


    /**
     * 开始时间
     */
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    @TableField("end_date")
    private LocalDateTime endDate;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8")
    private LocalDateTime singleDate;

    /**
     * 对应字段(对应数据库里的字段名称)
     */
    @TableField("db_name")
    private String dbName;

    /**
     * 对应字段(对应数据库里的字段名称)
     */
    private Integer sort;
}
