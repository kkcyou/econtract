package com.yaoan.module.econtract.dal.dataobject.gpx;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/29 15:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gpx_supplier_combination")
public class SupplierCombinationInfoDO  extends BaseDO {
    private static final long serialVersionUID = -8278692779230798322L;
    /**
     * id
     */
    @TableId(value = "id")
    private String id;
    /**
     * 报名ID（对应供应商报名表id）
     */
    private String signId;
    /**
     * 主体供应商ID
     */
    private String mainSupplierId;
    /**
     * 主体供应商名称
     */
    private String mainSupplierName;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 项目名称
     */
    private String projectName;
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
     * 是否主体：0 否 1 是
     */
    private Integer isSubject;
    /**
     * 是否有效：0 否 1 是
     */
    private Integer isStatus;
}
