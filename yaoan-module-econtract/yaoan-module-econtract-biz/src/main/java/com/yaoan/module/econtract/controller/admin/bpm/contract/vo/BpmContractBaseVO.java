package com.yaoan.module.econtract.controller.admin.bpm.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author doujiale
 */
@Data
public class BpmContractBaseVO {

    @Schema(description = "申请人的用户编号")
    private Long userId;

    @Schema(description = "合同信息标识")
    private String contractId;

    @Schema(description = "申请类型 参考字典 approve_type")
    private String approveType;

    @Schema(description = "合同编码")
    private String contractCode;

    @Schema(description = "合同名称")
    private String contractName;

    @Schema(description = "合同类型 标识")
    private String contractType;

    @Schema(description = "来源")
    private Integer upload;

    @Schema(description = "申请原因")
    private String reason;

    @Schema(description = "申请结果")
    private Integer result;

    @Schema(description = "提交时间")
    private LocalDateTime createTime;

}
