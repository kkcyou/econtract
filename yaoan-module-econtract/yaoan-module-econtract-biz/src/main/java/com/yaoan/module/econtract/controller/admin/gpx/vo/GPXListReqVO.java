package com.yaoan.module.econtract.controller.admin.gpx.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/22 10:00
 */
@Data
public class GPXListReqVO extends PageParam {

    private static final long serialVersionUID = -1141586363863754132L;
    /**
     * 招标项目方式名称：
     * common:一般项目采购、
     * batch:批量集中采购、
     * union:联合采购、
     * other:其他
     */
    private String biddingMethodCode;

    private String token;


    /**
     * 项目名称
     */
    @Schema(description = "项目名称")
    private String projectName;
    /**
     * 项目编号
     */
    @Schema(description = "项目编号")
    private String projectCode;
    /**
     * 项目类型（货物、服务、工程）
     */
    @Schema(description = "项目类型（货物、服务、工程）")
    private String projectType;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称")
    private String supplierName;
}
