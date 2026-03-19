package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.api.gcy.order.GoodsVO;
import com.yaoan.module.econtract.controller.admin.model.vo.TermsDetailsVo;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.econtract.enums.contract.ContractKindEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Schema(description = "Contract CreateReq VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractCreateReqVO extends ContractBaseVO {
    private static final long serialVersionUID = 5698384574355998466L;

    /**
     * id-主键
     */
    @Schema(description = "id-主键", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

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
     */
    @Schema(description = "签署状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    /**
     * 签署文件名称
     */
    @Schema(description = "签署文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;

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

    //上传合同 新增参数 ----------------------------

    /**
     * {@link ContractUploadTypeEnums}
     */
    @Schema(description = "创建合同方式： 0-草拟 1-上传", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer upload;

    /**
     * 签署类型： 0-电子签署 1-纸质签署
     */
    @Schema(description = "签署类型： 0-电子签署 1-纸质签署", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer signType;

    /**
     * 合同存放处
     */
    @Schema(description = "合同存放处", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String deposit;

    /**
     *签署日期
     */
    @Schema(description = "签署日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date signDate;

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
     * 归档
     * 0 - 未归档
     * 1 - 已归档
     */
    @Schema(description = "归档", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer document;

    /**
     *关联的模板id
     */
    @Schema(description = "关联的模板id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String templateId;

    /**
     * 是否发起审批 1:发起
     */
    @Schema(description = "是否发起审批", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer initiateApproval;

//------------------------------------ 第五期内蒙 ---------------------------------------

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
     * 合同参数信息集合
     */
    private List<ContractParameterVO> contractParameterVOList;

    /**
     * 合同章信息
     */
    private List<ContractSealVO> contractSealVOList;

    /**
     * 付款信息集合
     */
    private List<PaymentScheduleVO> paymentScheduleVOList;

    /**
     * 合同采购内容集合
     */
    private List<ContractPurchaseReqVO> contractPurchaseReqVOList;

    /**
     * 合同签订方信息集合
     */
    private List<ContractSignatoryReqVO> contractSignatoryReqVOList;
    /**
     * 订单id
     *
     */
    private String orderGuid;

    /**
     * 采购标的信息--ContractGoodsMapper
     */
    @Schema(description = "采购标的信息", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "采购标的信息不能为空")
    @Valid
    private List<GoodsVO> goodsList;
    /**
     * 合同来源（电子卖场：gpmall-5.3，电子交易（项目采购）：，框采平台：gp-gpfa）
     */
//    @NotBlank(message = "合同来源不能为空")
    @Schema(description = "合同来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractFrom;
    /**
     * 框架协议编号
     */
    @Schema(description = "协议编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agreementCode;
    /**
     * 供应商开户行账号
     */
    @Schema(description = "供应商开户行账号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankAccount;
    /**
     * 供应商开户行名称
     */
    @Schema(description = "供应商开户行名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankName;
    /**
     * 采购计划备案书/标准书编号
     */
    @Schema(description = "采购计划备案书/标准书编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanCode;
    /**
     * 甲方指派联系人 -- 采购人指派联系人 、甲方联系人对应 value13   采购人代表。甲方代表
     */
    @Schema(description = "甲方指派联系人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerLink;
    /**
     * 采购人负责人电话
     */
    @Schema(description = "采购人负责人电话", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerLinkMobile;
    /**
     * 采购人id
     */
    @Schema(description = "采购人/采购单位id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgId;
    /**
     * 采购人名称
     */
    @Schema(description = "采购人/采购单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgName;
    /**
     * 采购人负责人。甲方联系人
     */
    @Schema(description = "采购人负责人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerProxy;
    /**
     * 合同签订地址
     */
    @Schema(description = "合同签订地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractSignAddress;
    /**
     * 交货地址
     */
    @Schema(description = "交货地点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsDeliveryaddress;

    /**
     * send：是否发送 0：未发送（保存）  1：发送  2：确认
     */
    @Schema(description = "是否发送", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer send;
    /**
     *合同金额大写
     */
    @Schema(description = "合同金额大写", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String shiftMoney;
    /**
     * 供应商id
     */
    @Schema(description = "供应商id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierId;
    /**
     * 乙方指派联系人-供应商指派联系人 ，乙方联系人对应 value12 供应商（乙方）代表
     */
    @Schema(description = "乙方指派联系人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLink;
    /**
     * 供应商负责人电话
     */
    @Schema(description = "供应商负责人电话", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLinkMobile;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierName;
    /**
     * 供应商负责人
     */
    @Schema(description = "供应商负责人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierProxy;

    /**
     * 编辑类型
     */
    @Schema(description = "编辑类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer editType;

    /**
     * 智能审查文件id
     */
    @Schema(description = "智能审查文件id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String uploadFileAiId;

    /**
     * 条款列表
     */
    private List<TermsDetailsVo> terms;

    /**
     * 提交标识, 1,当为提交之前的保存时做字段必填校验
     */
    private Integer isSubmit;

    /**
     * 需要验收
     * */
    private Integer needAcceptance;

    /**
     * 相对方流程的规则设置（0=会签，1=依次签）
     * */
    private String flowSortRule;


    /**
     * 关联主合同id（广西大学招标要求）
     * 为了关联：主协议、补充协议、变更协议等
     */
    private String relativeMainContractId;

    /**
     * 合同类别
     * {@link ContractKindEnums}
     * 主合同、补充协议、框架协议
     */
    private Integer contractKind;

}
