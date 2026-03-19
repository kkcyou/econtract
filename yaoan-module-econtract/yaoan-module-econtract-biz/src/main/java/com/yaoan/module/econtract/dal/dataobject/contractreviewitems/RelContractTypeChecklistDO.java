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
 * 合同类型-审查清单关联 DO
 *
 * @author 芋道源码
 */
@TableName("ecms_rel_contract_type_checklist")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelContractTypeChecklistDO extends TenantBaseDO {

    /**
     * 关联id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 合同类型编号
     */
    private String contractType;
    /**
     * 审查清单id
     */
    private String reviewListId;

}