package com.yaoan.module.econtract.dal.dataobject.contractreviewitems;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 审查比对检测项 DO
 *
 * @author 芋道源码
 */
@TableName("ecms_review_compare_items")
@KeySequence("ecms_review_compare_items_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCompareItemsDO extends TenantBaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 一级检测标题
     */
    private String itemFirstLevel;
    /**
     * 二级检测标题
     */
    private String itemSecondLevel;
    /**
     * 检测项名称
     */
    private String itemName;

}