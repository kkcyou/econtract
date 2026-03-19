package com.yaoan.module.econtract.dal.dataobject.category;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * 合同类型分类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_type_category")
public class ContractTypeCategory extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = -3949100711897891427L;

    /**
     * 分类id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父分类id
     */
    private Integer parentId;

    /**
     * 分类编码
     */
    private String code;

    /**
     * 分类名称
     */
    private String name;
}
