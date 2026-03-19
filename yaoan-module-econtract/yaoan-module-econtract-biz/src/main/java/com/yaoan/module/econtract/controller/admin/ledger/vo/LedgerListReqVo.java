package com.yaoan.module.econtract.controller.admin.ledger.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @description: 台账列表Vo
 * @author: Pele
 * @date: 2023/8/22 13:44
 */
@Data
@Schema(description = "台账列表Vo")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LedgerListReqVo extends PageParam {

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     * 合同类型（all=全部、sales=销售合同、purchase=采购合同、rental =租赁合同、transportation =运输合同、warehousing =仓储合同 等）
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractType;

    /**
     * 合同状态（含义参见ContractStatusEnums）
     */
    @Schema(description = "合同状态")
    private Integer contractStatus;

    /**
     * 相对方签约主体（乙方）
     */
    @Schema(description = "相对方签约主体（乙方）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    private String counterparty;

    /**
     * 合同签订时间起始
     */
    @Schema(description = "合同签订时间起始", example = "2021-07-11 16:02:57")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8")
    private Date signTimeStart;

    /**
     * 合同签订时间终止
     */
    @Schema(description = "合同签订时间终止",  example = "2021-07-11 16:02:57")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8")
    private Date signTimeEnd;

    /**
     * 合同归档时间起始
     */
    @Schema(description = "合同归档时间起始", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8")
    private Date filingTimeStart;

    /**
     * 合同归档时间终止
     */
    @Schema(description = "合同归档时间终止", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8")
    private Date filingTimeEnd;

    /**
     * 合同签署方式
     */
    @Schema(description = "合同签署方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-07-11 16:02:57")
    private String signType;


}
