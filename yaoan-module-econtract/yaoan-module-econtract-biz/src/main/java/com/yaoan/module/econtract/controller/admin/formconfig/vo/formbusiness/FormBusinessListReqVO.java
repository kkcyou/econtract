package com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/19 19:03
 */
@Data
public class FormBusinessListReqVO extends PageParam {

    private static final long serialVersionUID = 1672673849248392938L;
    private String id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 业务类型
     */
    @TableField("business_type")
    private String businessType;

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
}
