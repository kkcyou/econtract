package com.yaoan.module.econtract.dal.dataobject.businesstype;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

/**
 * 业务类型 DO
 *
 * @author lls
 */
@TableName("ecms_business_type")
@KeySequence("ecms_business_type_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessTypeDO extends TenantBaseDO {

    /**
     * 单据类型id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 单据类型编号
     */
    private String code;
    /**
     * 单据类型名称
     */
    private String name;
    /**
     * 数据表
     */
    private String tableName;

}