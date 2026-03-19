package com.yaoan.module.econtract.controller.admin.gpx.vo.autofill;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/6/2 22:40
 */
@Data
public class PackageRespVO {

    /**
     * 包id
     */
    private String packageGuid;
    /**
     * 包号
     */
    private Integer packageNumber;
    /**
     * 包名称
     */
    private String packageName;
    /**
     * 包金额
     */
    private Double amount;
    /**
     * 中标价格
     */
    private Double winBidAmount;
    /**
     * 中标时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date winBidTime;

    /**
     * 所属的项目guid
     */
    private String projectGuid;

    /**
     * 项目负责人
     */
    private String managerId;
    /**
     * 项目负责人名称
     */
    private String managerName;

    /**
     * 代理机构名称
     */
    private String agencyName;
    /**
     * 代理机构ID
     */
    private String agencyId;

    /**
     * 是否PPP项目(1:是,0:否)
     */
    private Integer isPpp;

    /**
     * 采购方式编码
     */
    private String purchaseMethodCode;
    /**
     * 采购方式名称
     */
    private String purchaseMethodName;

    /**
     * 区域编码
     */
    private String zoneCode;
    /**
     * 区域名称
     */
    private String zoneName;
}
