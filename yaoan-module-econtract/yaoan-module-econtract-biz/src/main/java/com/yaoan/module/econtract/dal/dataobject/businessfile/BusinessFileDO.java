package com.yaoan.module.econtract.dal.dataobject.businessfile;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

import java.io.Serializable;

/**
 * 业务数据和附件关联关系 DO
 *
 * @author lls
 */
@TableName("ecms_business_file")
@KeySequence("ecms_business_file_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessFileDO extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = -8660076061706670928L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 业务表id
     */
    private String businessId;
    /**
     * 附件id
     */
    private Long fileId;
    /**
     * 附件名称
     */
    private String fileName;
}