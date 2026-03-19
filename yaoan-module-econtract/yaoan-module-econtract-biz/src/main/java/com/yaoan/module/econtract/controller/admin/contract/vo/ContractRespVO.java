package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
import com.yaoan.module.econtract.controller.admin.agreement.vo.PrefAgreementRelRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.BorrowRecordRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ledger.BpmContractChangeRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ledger.ContractBorrowRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ledger.ContractFileRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ledger.PayRecordRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.out.AcceptanceRecordRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.TermsDetailsVo;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.PerformanceTaskInfoRespVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativeByUserRespVO;
import com.yaoan.module.econtract.controller.admin.signet.vo.SignetManageRespVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractParameterDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractPurchaseDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractSealDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractSignatoryDO;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.contract.ContractKindEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Schema(description = "Contract CreateReq VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractRespVO extends ContractBaseVO {
    private static final long serialVersionUID = -8910745421029870755L;
    /**
     * 合同变动信息
     */
    List<BpmContractChangeRespVO> contractChangeList;
    /**
     * 签署流程信息
     */
    List<BpmProcessRespDTO> bpmProcessRespDTOList;
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
     * 签署方集合
     */
    @Schema(description = "签署方集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<RelativeByUserRespVO> signatoryList;
    /**
     * 合同分类名称
     */
    @Schema(description = "合同分类名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractCategoryName;
    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractTypeName;
    /**
     * 补充协议集合
     */
    @Schema(description = "补充协议集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<PrefAgreementRelRespVO> agreements;
    /**
     * 合同终止文件名称
     */
    @Schema(description = "合同终止文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String terminationFileName;
    /**
     * 合同终止文件地址
     */
    @Schema(description = "合同终止文件地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long terminationFileAddId;
    /**
     * 履约任务信息
     */
    @Schema(description = "履约任务信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<PerformanceTaskInfoRespVO> performanceTaskInfoRespVOList;
    /**
     * 主文件地址id
     */
    @Schema(description = "主文件地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long fileAddId;
    /**
     * 主文件地址url
     */
    @Schema(description = "主文件地址url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileUrl;
    private String pdfFileUrl;
    /**
     * 任务id
     */
    @Schema(description = "任务id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String taskId;
    /**
     * 归档
     * 0 - 未归档
     * 1 - 已归档
     * 2- 归档中
     */
    private Integer document;

    @Schema(description = "档案id")
    private String archiveId;
    /**
     * 归档人员名称
     */
    @Schema(description = "归档人员名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String documentName;
    /**
     * 归档日期
     */
    @Schema(description = "归档日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime documentDate;
    /**
     * 归档文件名称
     */
    @Schema(description = "归档文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String documentFileName;
    /**
     * 归档文件地址id
     */
    @Schema(description = "归档文件地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long documentAddId;

    /**
     * 归档附件集合
     */
    @Schema(description = "归档附件集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<AttachmentRelRespVO> documentAttachmentList;
    /**
     * 签署日期
     */
    @Schema(description = "签署日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date signDate;
    /**
     * 合同有效期-开始时间
     */
    @Schema(description = "合同有效期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date validity0;
    /**
     * 合同有效期-结束时间
     */
    @Schema(description = "合同有效期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date validity1;
    /**
     * 金额类型： 0 支出 1 收入
     */
    @Schema(description = "金额类型： 0支出 1收入", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer amountType;

    /**
     * 金额类型： 0 支出 1 收入
     */
    @Schema(description = "金额类型： 0支出 1收入", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String amountTypeName;
    /**
     * 合同金额
     */
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double amount;
    /**
     * 合同文件pdf 地址id
     */
    @Schema(description = "合同文件pdf地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pdfFileId;
    /**
     * 合同借阅文件地址
     */
    @Schema(description = "合同借阅文件地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long borrowFileId;
    /**
     * 合同文件信息
     */
    @Schema(description = "合同文件信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ContractInfoVO contractInfo;
    /**
     * 合同参数信息
     */
    private List<ContractParameterDO> contractParameterDOList;
    /**
     * 合同章信息
     */
    private List<ContractSealDO> contractSealDOList;
    /**
     * 合同付款计划信息
     */
    private List<PaymentScheduleRespVO> paymentScheduleDOList;
    /**
     * 合同采购内容信息
     */
    private List<ContractPurchaseDO> contractPurchaseDOList;
    /**
     * 合同签定方信息
     */
    private List<ContractSignatoryDO> contractSignatoryDOList;
    /**
     * 上传合同
     * 默认
     * 0 - 模板起草
     * 1 - 上传文件起草
     * 2 - 合同补录 上传文件
     * 3 - 依据已成交的采购项目或订单起草
     */
    private Integer upload;
    /**
     * 计划编号
     */
    private String buyPlanCode;
    /**
     * 签约地点
     */
    private String location;
    /**
     * 合同有效期
     */
    private Long validity;
    /**
     * 涉密条款 1是 0否
     */
    private Integer secrecyClause;
    /**
     * 备注
     */
    private String contractDescription;
    /**
     * 借阅记录集合
     */
    private List<BorrowRecordRespVO> borrowRecordList;
    /**
     * 签署状态
     * 0-待发送 - 新增
     * 1-被退回
     * 2-已发送
     * 3-待确认
     * 4-待签署
     * 5-已拒签
     * 6-签署完成
     * 7-逾期未签署
     * 8-合同终止签署中
     * 9-合同终止
     * 10-合同变更
     * 11-待送审
     * 12-审核中
     * 13审核未通过
     */
    private Integer status;
    private String statusName;

    /**
     * 合同存放处
     */
    private String deposit;

    /**
     * 甲方名称
     */
    private String partAName;

    /**
     * 乙方名称
     */
    private String partBName;

    /**
     * 签署类型
     * 0 - 电子签署
     * 1 - 纸质签署
     */
    private Integer signType;

    /**
     * 合同审批信息
     */
    private List<BpmProcessRespDTO> bpmProcessRespVOList;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 编辑类型
     * 0 = rtf
     * 1 = office
     */
    private Integer editType;


    @Schema(description = "完成交易生成合同的交易平台的代码/合同来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String platform;

    private String platformName;


    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 采购人是否签署
     */
    private Integer isSign;

    /**
     * 是否线下(默认0)
     * {@link IfNumEnums}
     */
    private Integer isOffline;
    /**
     * 是否线下签署(默认0)
     * {@link IfNumEnums}
     */
    private Integer isOfflineSign;
    /**
     * 初始合同文件pdf 地址
     */
    private Long oldPdfFileId;
    /**
     * 条款列表
     */
    private List<TermsDetailsVo> terms;
    /**
     * 正文&附件
     */
    private ContractFileRespVO fileRespVO;

    /**
     * 验收记录
     */
    private List<AcceptanceRecordRespVO> acceptanceRespVOList;

    /**
     * 借阅记录
     */
    private List<ContractBorrowRespVO> borrowRecordRespVOList;

    private String creator;

    private String creatorName;

    /**
     * 是否已申请解除
     * */
    private Integer appliedTerminate;

    /**
     * 流程实例的编号
     */
    private String processInstanceId;
    private SignetManageRespVO signetManageRespVO;
    /**
     * 付款记录
     */
    private List<PayRecordRespVO> payApplicationRespVOList;
    /**
     * 收款记录
     */
    private List<PayRecordRespVO> collectApplicationRespVOList;

    /**
     * 相对方流程的规则设置（0=会签，1=依次签）
     * */
    private String flowSortRule;


    /**
     * 智能审查文件id
     */
    @Schema(description = "智能审查文件id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String uploadFileAiId;

    /**
     * 关联主合同id（广西大学招标要求）
     * 为了关联：主协议、补充协议、变更协议等
     */
    private String relativeMainContractId;
    /**
     * 关联主合同id（广西大学招标要求）
     * 为了关联：主协议、补充协议、变更协议等
     */
    private String relativeMainContractName;
    /**
     * 合同类别
     * {@link ContractKindEnums}
     * 主合同、补充协议、框架协议
     */
    private Integer contractKind;

    /**
     * 是否是相对方
     */
    private Integer isRelative;

}
