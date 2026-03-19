package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Schema(description = "Signet CreateReq VO")
@Data
@ToString(callSuper = true)
public class EmpowerCreateReqVO {

    /**
     * 印章授权id
     */
    @Schema(description = "印章授权id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String empowerId;

    /**
     * 印章id
     */
    @Schema(description = "印章id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "印章不能为空")
    private String sealId;

    /**
     * 授权对象id
     */
    @Schema(description = "授权对象id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "授权对象不能为空")
    private String empowerObjectId;

    /**
     * 授权范围（全部合同用0，模版使用模版id）
     */
    @Schema(description = "授权范围", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "授权范围不能为空")
    private String empowerRange;

    /**
     * 是否长期有效（0：否，1：是）
     */
    @Schema(description = "是否长期有效", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isPermanent;

    /**
     * 授权开始时间
     */
    @Schema(description = "授权开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date empowerStartDate;

    /**
     * 授权结束时间
     */
    @Schema(description = "授权结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date empowerEndDate;

}
