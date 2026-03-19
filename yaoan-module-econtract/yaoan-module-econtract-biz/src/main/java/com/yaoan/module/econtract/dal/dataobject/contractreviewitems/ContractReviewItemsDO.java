package com.yaoan.module.econtract.dal.dataobject.contractreviewitems;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.econtract.enums.contractreviewitems.RiskLevelEnums;
import lombok.*;

/**
 * 合同审查规则 DO
 *
 * @author admin
 */
@TableName("ecms_contract_review_items")
@KeySequence("ecms_contract_review_items_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractReviewItemsDO extends TenantBaseDO {

    /**
     * 规则ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 审查内容
     */
    private String reviewContent;
    /**
     * 所属条款
     */
    private Integer termId;

    /**
     * 风险不利方
     */
    private Integer riskParty;
    /**
     * 风险等级
     * 枚举 {@link RiskLevelEnums}
     */
    private Integer riskLevel;
    /**
     * 备注
     */
    private String notes;

    /**
     * 状态
     */
    private Integer reviewStatus;

}
