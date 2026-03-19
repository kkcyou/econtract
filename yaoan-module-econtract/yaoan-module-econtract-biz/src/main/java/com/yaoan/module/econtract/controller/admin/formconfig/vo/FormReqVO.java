package com.yaoan.module.econtract.controller.admin.formconfig.vo;

import com.yaoan.module.econtract.controller.admin.formconfig.vo.item.FormItemReqVO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/19 11:15
 */
@Data
public class FormReqVO {
    private String id;
    private String businessId;
    /**
     * 表单编号
     */
    private String code;

    /**
     * 表单名称
     */
    private String name;

    /**
     * 表单分类
     */
    private String formCategory;

    /**
     * 表单类型
     */
    private String formType;

    /**
     * 状态 0=停用 1=启用
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 表项集合
     */
    private List<FormItemReqVO> itemReqVOS;

    /**
     * 排序(表单的排序存入关系表中)
     */
    private Integer sort;
}
