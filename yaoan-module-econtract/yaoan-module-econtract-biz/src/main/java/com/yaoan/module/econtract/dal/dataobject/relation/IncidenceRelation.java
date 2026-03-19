package com.yaoan.module.econtract.dal.dataobject.relation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * 关联关系实体类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_incidence_relation")
public class IncidenceRelation  extends TenantBaseDO implements Serializable {
    private static final long serialVersionUID = -9118560040538782839L;
    /**
     * 关联关系id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 关联合同id
     */
    private String relationContractId;
    /**
     * 关联关系
     */
    private Integer incidenceRelation;

}
