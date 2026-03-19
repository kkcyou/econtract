package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class UploadInfoVO {

    /**
     *签署日期
     */
    @Schema(description = "签署日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date signDate;

    /**
     *合同有效期-开始时间
     */
    @Schema(description = "合同有效期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date validity0;

    /**
     *合同有效期-结束时间
     */
    @Schema(description = "合同有效期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date validity1;

    /**
     *金额类型： 0 支出 1 收入
     */
    @Schema(description = "金额类型： 0支出 1收入", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer amountType;

    /**
     *合同金额
     */
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double amount;

    /**
     *签署方，我方名称
     */
    @Schema(description = "签署方，我方名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String mySignatory;

    /**
     *我方签署人名称
     */
    @Schema(description = "我方签署人名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String signName;
}
