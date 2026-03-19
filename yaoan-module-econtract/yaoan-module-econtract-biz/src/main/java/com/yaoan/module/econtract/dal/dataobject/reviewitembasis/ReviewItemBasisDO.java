package com.yaoan.module.econtract.dal.dataobject.reviewitembasis;

import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 合同审查规则依据 DO
 *
 * @author admin
 */
@TableName("ecms_review_item_basis")
@KeySequence("ecms_review_item_basis_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewItemBasisDO extends TenantBaseDO {

    /**
     * 依据ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 依据类型
     */
    private String type;
    /**
     * 依据标题
     */
    private String title;
    /**
     * 依据内容
     */
    private String content;
    /**
     * 规则id
     */
    private String reviewId;

}
