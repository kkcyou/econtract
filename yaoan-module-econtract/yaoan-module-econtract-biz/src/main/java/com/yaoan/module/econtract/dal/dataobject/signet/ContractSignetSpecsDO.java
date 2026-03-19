package com.yaoan.module.econtract.dal.dataobject.signet;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

/**
 * @Description 印章规格表
 * @Author  WSH
 * @Date: 2024-10-08 11:24:38
 */

@Data
@TableName("ecms_contract_signet_specs")
@EqualsAndHashCode(callSuper = true)
public class ContractSignetSpecsDO extends DeptBaseDO {
    private static final long serialVersionUID = 1728357878375L;

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 印章编码
     */
    private String code;

    /**
     * 印章形状
     */
    private String shape;

    /**
     * 长方形/椭圆/正方形的长度（mm）
     */
    private Double high;

    /**
     * 长方形/椭圆/正方形的宽度（mm）
     */
    private Double width;

}
