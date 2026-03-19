package com.yaoan.module.econtract.dal.dataobject.review;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_review")
public class ReviewListDO extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = 4059046527572673198L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 审查清单名称
     */
    private String name;

    /**
     * 审查点排列
     */
    private Integer pointsArray;

    /**
     * 备注
     */
    private String remark;
}

