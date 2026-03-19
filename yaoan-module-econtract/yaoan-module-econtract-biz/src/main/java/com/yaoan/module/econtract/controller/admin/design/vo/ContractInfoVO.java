package com.yaoan.module.econtract.controller.admin.design.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.agreement.vo.PrefAgreementRelRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.AttachmentRelRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractBaseVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.SignatoryRespVO;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.PerformanceTaskInfoRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author doujiale
 */
@Schema(description = "Contract CreateReq VO")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractInfoVO extends ContractBaseVO {

    private static final long serialVersionUID = -3810037071825710636L;

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    /**
     * 合同文件内容
     */
    @NotBlank(message = "合同文件内容不可为空")
    @Schema(description = "合同文件")
    private String contractContent;

    /**
     * 附件集合
     */
    @Schema(description = "附件集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<AttachmentRelRespVO> attachmentList;

    /**
     *签署方集合
     */
    @Schema(description = "签署方集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SignatoryRespVO> signatoryList;

    /**
     *合同分类名称
     */
    @Schema(description = "合同分类名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractCategoryName;

    /**
     *合同类型名称
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractTypeName;

    /**
     *补充协议集合
     */
    @Schema(description = "补充协议集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<PrefAgreementRelRespVO> agreements;

    /**
     *合同终止文件名称
     */
    @Schema(description = "合同终止文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String terminationFileName;

    /**
     *合同终止文件地址
     */
    @Schema(description = "合同终止文件地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long terminationFileAddId;

    /**
     *履约任务信息
     */
    @Schema(description = "履约任务信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<PerformanceTaskInfoRespVO> performanceTaskInfoRespVOList;

    /**
     *主文件地址id
     */
    @Schema(description = "主文件地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long fileAddId;

    /**
     *主文件地址url
     */
    @Schema(description = "主文件地址url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileUrl;

    /**
     *任务id
     */
    @Schema(description = "任务id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String taskId;

    /**
     *归档人员名称
     */
    @Schema(description = "归档人员名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String documentName;

    /**
     *归档日期
     */
    @Schema(description = "归档日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime documentDate;

    /**
     *归档文件名称
     */
    @Schema(description = "归档文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String documentFileName;

    /**
     *归档文件地址id
     */
    @Schema(description = "归档文件地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long documentAddId;

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

    /**
     * 合同文件pdf 地址id
     */
    @Schema(description = "合同文件pdf地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pdfFileId;

    /**
     * 合同文件信息
     */
    @Schema(description = "合同文件信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ContractInfoVO contractInfo;
}
