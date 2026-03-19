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
 * 合同结算类型
 *
 * @author lls
 * @since 2024-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_status_category")
public class ContractSettlementCategory extends TenantBaseDO implements Serializable {


    private static final long serialVersionUID = 631604604719768201L;
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
     * 分类名称
     */
    private String name;

    /**
     * 分类编码
     */
    private String code;

}
