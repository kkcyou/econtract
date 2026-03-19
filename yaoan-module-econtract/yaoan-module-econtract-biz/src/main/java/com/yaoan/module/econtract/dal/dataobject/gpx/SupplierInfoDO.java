package com.yaoan.module.econtract.dal.dataobject.gpx;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/21 18:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gpx_supplier")
public class SupplierInfoDO extends BaseDO {
    private static final long serialVersionUID = 5875656008668821806L;
    /**
     * id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 项目id
     */
    private String projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 包id
     */
    private String packageId;
    /**
     * 包号
     */
    private String packageNumber;
    /**
     * 包名称
     */
    private String packageName;
    /**
     * 供应商id
     */
    private String supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 中标金额
     */
    private String winBidAmount;

}
