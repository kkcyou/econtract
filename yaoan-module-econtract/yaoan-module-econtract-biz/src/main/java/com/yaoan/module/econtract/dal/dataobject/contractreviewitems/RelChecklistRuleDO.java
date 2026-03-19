package com.yaoan.module.econtract.dal.dataobject.contractreviewitems;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 审查清单-审查规则关联 DO
 *
 * @author 芋道源码
 */
@TableName("ecms_rel_checklist_rule")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelChecklistRuleDO extends TenantBaseDO {

    /**
     * 关联id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 审查规则id
     */
    private String reviewId;
    /**
     * 审查清单id
     */
    private String reviewListId;

}