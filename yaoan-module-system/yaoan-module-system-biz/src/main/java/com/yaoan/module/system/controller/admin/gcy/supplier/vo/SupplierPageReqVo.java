package com.yaoan.module.system.controller.admin.gcy.supplier.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SupplierPageReqVO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "管理后台 - 供应商列表 RespVO")
public class SupplierPageReqVo  extends PageParam {

    private static final long serialVersionUID = -8811475729583702611L;
    /**
     * 关键字检索
     */
    private String name;
}
