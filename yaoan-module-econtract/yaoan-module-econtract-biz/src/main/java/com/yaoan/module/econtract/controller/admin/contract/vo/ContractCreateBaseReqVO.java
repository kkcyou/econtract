package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Schema(description = "Contract CreateReq VO")
@Data
@ToString(callSuper = true)
public class ContractCreateBaseReqVO implements Serializable{
    /**
     * id-主键
     */
    @Schema(description = "id-主键", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 签署截止日期
     */
    @Schema(description = "签署截止日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date expirationDate;

    /**
     * 合同分类
     */
    @Schema(description = "合同分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractCategory;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractType;

    /**
     * 合同描述
     */
    @Schema(description = "合同描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractDescription;

    /**
     * 关联的模板id
     */
    @Schema(description = "关联的模板id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String templateId;

    /**
     * 签署文件地址id
     */
    @Schema(description = "签署文件地址id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fileAddId;

    /**
     * 合同文件pdf 地址id
     */
    @Schema(description = "合同文件pdf地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pdfFileId;

    /**
     * 合同文件内容
     */
    @Schema(description = "合同文件")
    private String contractContent;

    /**
     * 附件集合
     */
    @Schema(description = "附件集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<AttachmentRelCreateReqVO> attachmentList;

    /**
     * 签署方id集合
     */
    @Schema(description = "签署方id集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SignatoryRelReqVO> signatoryList;

    /**
     * {@link ContractUploadTypeEnums}
     */
    @Schema(description = "创建合同方式： 0-草拟 1-上传", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer upload;

    /**
     *合同有效期-开始时间
     */
    @Schema(description = "合同有效期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date validity0;

    /**
     *合同有效期-结束时间
     */
    @Schema(description = "合同有效期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date validity1;

    /**
     *合同金额
     */
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double amount;

    /**
     * 合同参数信息集合
     */
    private List<ContractParameterVO> contractParameterVOList;


    /**
     * 付款信息集合
     */
    private List<PaymentScheduleVO> paymentScheduleVOList;
}
