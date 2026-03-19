package com.yaoan.module.econtract.controller.admin.formconfig.form.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.item.FormItemRespVO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/20 17:35
 */
@Data
public class FormOneRespVO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 表单编号
     */
    @TableField("code")
    private String code;

    /**
     * 表单名称
     */
    @TableField("name")
    private String name;

    /**
     * 表单分类
     */
    @TableField("form_category")
    private String formCategory;

    /**
     * 表单类型
     */
    @TableField("form_type")
    private String formType;

    /**
     * 状态 0=停用 1=启用
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 表项集合
     */
    private List<FormShowItemRespVO> itemReqVOList;
}
