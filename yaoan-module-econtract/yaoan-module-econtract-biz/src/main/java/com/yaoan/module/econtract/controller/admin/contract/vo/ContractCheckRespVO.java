package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.agreement.vo.PrefAgreementRelRespVO;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.PerformanceTaskInfoRespVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativeByUserRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class ContractCheckRespVO {
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
    @JsonFormat(pattern = "yyyy-MM-dd")
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
     * 附件集合
     */
    @Schema(description = "附件集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<AttachmentRelRespVO> attachmentList;

    /**
     *签署方集合
     */
    @Schema(description = "签署方集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<RelativeByUserRespVO> signatoryList;

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
     *任务id
     */
    @Schema(description = "任务id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String taskId;

    /**
     * 归档文件信息
     */
    @Schema(description = "归档文件信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DocumentInfoVO documentInfo;

    /**
     * 存量上传文件信息
     */
    @Schema(description = "存量上传文件信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private UploadInfoVO uploadInfo;

    /**
     * 合同文件信息
     */
    @Schema(description = "合同文件信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ContractInfoVO contractInfo;
    /**
     * 计划编号
     */
    private String buyPlanCode;
}
