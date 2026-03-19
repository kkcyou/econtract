package com.yaoan.module.econtract.dal.dataobject.businessformfield;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

/**
 * 表单字段 DO
 *
 * @author lls
 */
@TableName("ecms_business_form_field")
@KeySequence("ecms_business_form_field_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessFormFieldDO extends TenantBaseDO {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 编码
     */
    private String fieldCode;
    /**
     * 名称
     */
    private String fieldName;
    /**
     * 所属表单id
     */
    private String formId;
    /**
     * 所属业务id
     */
    private String businessId;
    /**
     * 是否展示
     */
    private Integer isShow;
    /**
     * 是否在搜索中展示
     */
    private Integer isSearch;

}