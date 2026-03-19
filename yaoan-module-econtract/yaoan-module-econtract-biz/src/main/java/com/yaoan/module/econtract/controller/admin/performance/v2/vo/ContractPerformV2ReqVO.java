package com.yaoan.module.econtract.controller.admin.performance.v2.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;

import java.util.Date;

/**
 * @description: 合同履约列表ReqVO
 * @author: Pele
 * @date: 2024/9/23 14:15
 */
@Data
public class ContractPerformV2ReqVO extends PageParam {

    private static final long serialVersionUID = -3318295307620165367L;

    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 合同状态
     * {@link com.yaoan.module.econtract.enums.ContractStatusEnums}
     */
    private Integer status;
    /**
     * 签署完成时间
     */
    private String performanceSignedDate;
    /**
     * 履约截止时间
     */
    private String performanceExpirationDate;

    /**
     * 签署完成时间0
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date performanceSignedDateDate0;
    /**
     * 签署完成时间1
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date performanceSignedDateDate1;
}
