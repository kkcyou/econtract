package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.Date;
@Schema(description = "Contract PageRequest VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractDocumentPageReqVO extends PageParam {
    private static final long serialVersionUID = -1459049987170790129L;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String code;

    /**
     *合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     *签署截止日期-开始时间
     */
    @Schema(description = "签署截止日期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date expirationDate0;

    /**
     *签署截止日期-结束时间
     */
    @Schema(description = "签署截止日期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date expirationDate1;

    /**
     *合同分类
     */
    @Schema(description = "合同分类", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer contractCategory;

    /**
     *合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractType;

    /**
     *上传日期开始时间
     */
    @Schema(description = "上传日期开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date uploadDate0;

    /**
     *上传日期结束时间
     */
    @Schema(description = "上传日期结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date uploadDate1;

    /**
     *签署日期开始时间
     */
    @Schema(description = "签署日期开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date signDate0;

    /**
     *签署日期结束时间
     */
    @Schema(description = "签署日期结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date signDate1;

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
     *归档状态： 0未归档 1已归档
     */
    @Schema(description = "归档状态： 0未归档 1已归档", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer document;
}
