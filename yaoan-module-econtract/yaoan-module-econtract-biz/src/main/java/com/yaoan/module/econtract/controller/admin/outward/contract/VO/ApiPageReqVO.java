package com.yaoan.module.econtract.controller.admin.outward.contract.VO;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class ApiPageReqVO extends PageParam implements Serializable {
    private static final long serialVersionUID = 0401766761263307114233L;

    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractName;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractCode;

    /**
     * 合同状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;

    /**
     * 合同生效日期(yyyy-MM-dd) 开始时间
     */
    @Schema(description = "合同生效日期开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String effectDateStart;

    /**
     * 合同生效日期(yyyy-MM-dd) 结束时间
     */
    @Schema(description = "合同生效日期结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String effectDateEnd;

    /**
     * 合同终止日期(yyyy-MM-dd) 开始时间
     */
    @Schema(description = "合同终止日期开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String expiryDateStart;

    /**
     * 合同终止日期(yyyy-MM-dd) 结束时间
     */
    @Schema(description = "合同终止日期结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String expiryDateEnd;

}
