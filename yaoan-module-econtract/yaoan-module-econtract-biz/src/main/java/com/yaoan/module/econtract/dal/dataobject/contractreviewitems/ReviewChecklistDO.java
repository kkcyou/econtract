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
 * 审查清单 DO
 *
 * @author 芋道源码
 */
@TableName("ecms_review_checklist")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewChecklistDO extends TenantBaseDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 审查清单名称
     */
    private String reviewListName;
    /**
     * 审查清单类型code
     */
    private String reviewListCode;
    /**
     * 审查清单类型名称
     */
    private String reviewListType;
    /**
     * 状态 0开启 1关闭
     */
    private Boolean status;
    /**
     * 备注
     */
    private String notes;

}