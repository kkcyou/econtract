package com.yaoan.module.econtract.dal.dataobject.gcy.buyplan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 单一来源分包拟选定的供应商表
 * </p>
 *
 * @author doujiale
 * @since 2024-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_single_notice_supplier")
public class EcmsGcySingleNoticeSupplier extends BaseDO {

    private static final long serialVersionUID = -6952234927564628214L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 采购计划唯一识别码
     */
    private String buyPlanGuid;

    /**
     * 分包的唯一识别码
     */
    private String buyPlanPackageGuid;

    /**
     * 供应商唯一识别码(自有交易平台使用，第三方平台为NULL)
     */
    private String supplierGuid;

    /**
     * 供应商社会信用代码
     */
    private String supplierLicense;

    /**
     * 供应商名称
     */
    private String supplierName;

}
