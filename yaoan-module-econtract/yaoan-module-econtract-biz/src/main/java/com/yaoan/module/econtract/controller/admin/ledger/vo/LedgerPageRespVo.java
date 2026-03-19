package com.yaoan.module.econtract.controller.admin.ledger.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @description: 台账列表展示
 * @author: Pele
 * @date: 2023/8/22 13:51
 */
@Data
@Schema(description = "台账列表展示")
@ToString(callSuper = true)
public class LedgerPageRespVo {
    /**
     * 台账id
     */
    @Schema(description = "台账id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    private String id;

    /**
     * 合同编号
     */
    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    private String code;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    private String name;

    /**
     * 合同类型id
     */
    @Schema(description = "合同类型id")
    private String contractId;

    /**
     * 合同类型（all=全部、sales=销售合同、purchase=采购合同、rental =租赁合同、transportation =运输合同、warehousing =仓储合同 等）
     */
    @Schema(description = "合同类型名称")
    private String contractTypeName;

    @Schema(description = "合同类型id")
    private String contractType;

    /**
     * 合同状态（all=全部，signing=签署中，signed=签署完成，unsigned=签署未完成，overdue_unsigned=逾期未签署、voiding=作废中、voided=已作废、terminating=终止中、terminated=已终止、filed=已归档、fufilling=履约中、overdue_fufilling=履约超期、pause_fulfil=履约暂停、finished=合同完成、overdue_finished=合同超期完成）
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    private Integer contractStatus;

    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    private String contractStatusStr;

    /**
     * 合同金额
     */
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    private BigDecimal contractFinance;

    /**
     * 相对方签约主体（乙方）ID
     */
    @Schema(description = "相对方签约主体（乙方）ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    private String counterparty;

    /**
     * 相对方签约主体（乙方）
     */
    @Schema(description = "相对方签约主体（乙方）名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    private String counterpartyName;

    /**
     * 合同签订时间
     */
    @Schema(description = "合同签订时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime signTime;

    /**
     * 合同归档时间
     */
    @Schema(description = "合同归档时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime filingTime;

    /**
     * 该合同相关变动协议的个数
     */
    private Integer changeCount;
}
