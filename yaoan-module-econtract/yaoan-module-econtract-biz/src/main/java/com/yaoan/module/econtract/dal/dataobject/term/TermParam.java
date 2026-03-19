package com.yaoan.module.econtract.dal.dataobject.term;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author doujiale
 * @since 2023-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_term_param")
public class TermParam extends TenantBaseDO implements Serializable {


    private static final long serialVersionUID = -6059695423134448572L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同条款id
     */
    private String termId;

    /**
     * 合同参数id
     */
    private String paramId;

    /**
     * 合同参数序号
     */
    private Integer paramNum;

}
