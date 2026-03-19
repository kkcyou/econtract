package com.yaoan.module.econtract.dal.dataobject.contractreviewitems;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

/**
 * 合同审查规则 DO
 *
 * @author admin
 */
@TableName("ecms_review_contract_type_rel")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewContractTypeRelDO extends TenantBaseDO {

    /**
     * UUID主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 审查项ID
     */
    private String reviewId;

    /**
     * 合同类型ID
     */
    private Integer contractTypeId;

    /**
     * 合同类型名称
     */
    private String contractTypeName;
}
