
package com.yaoan.module.econtract.controller.admin.supervise.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 创建合同请求参数
  */
@Data
public class ContactBaseVO {
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;
    /**
     * 分包的唯一识别码
      */
    @Schema(description = "分包的唯一识别码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "包id不能为空")
    private String bidGuid;
    /**
     * 包编号
     */
    @Schema(description = "包编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotBlank(message = "包编号不能为空")
    private String bidCode;
    /**
     * 包名称
     */
    @Schema(description = "包名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotBlank(message = "包名称不能为空")
    private String bidName;
    /**
     * 采购单位id
     */
    @Schema(description = "采购单位id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购单位id不能为空")
    private String buyerOrgId;
    /**
     * 采购单位编码
     */
    @Schema(description = "采购单位编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购单位编码不能为空")
    private String orgCode;
    /**
     * 供应商id
     */
    @Schema(description = "供应商id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采供应商id不能为空")
    private String supplierId;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商名称不能为空")
    private String supplierName;
    /**
     * 采购人联系电话
      */
    @Schema(description = "采购人联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购人联系电话不能为空")
    private String buyerLinkMobile;
    /**
     * 采购负责人
      */
    @Schema(description = "采购负责人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购负责人不能为空")
    private String buyerProxy;
    /**
     * 采购计划id
      */
    @Schema(description = "采购计划id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购计划id不能为空")
    private String buyPlanGuid;


    /**
     * 合同文本是否包含涉密条款
      */
    @Schema(description = "合同文本是否包含涉密条款", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同文本是否包含涉密条款不能为空")
    private Integer clauseSecret;
    /**
     * 合同编码
      */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "合同编码不能为空")
    private String code;
    /**
     * 合同名称
      */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "合同名称不能为空")
    private String name;

    /**
     * 采购人联系地址
      */
    @Schema(description = "采购人联系地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购人联系地址不能为空")
    private String deliveryAddress;
    /**
     * 外商投资类型
      */
    @Schema(description = "外商投资类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String foreignInvestmentType;

    /**
     * 是否涉及政府购买服务，0：否，1：是
      */
    @Schema(description = "是否涉及政府购买服务", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否涉及政府购买服务不能为空")
    private Integer govService;
    /**
     * 是否涉及绿色建材采购，0：否，1：是
      */
    @Schema(description = "是否涉及绿色建材采购", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否涉及绿色建材采购不能为空")
    private Integer greenMaterial;
    /**
     * 是否高校、科研院所科研设备采购 ，0：否，1：是
     */
    @Schema(description = "是否高校、科研院所科研设备采购 ", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否高校、科研院所科研设备采购 不能为空")
    private Integer isSchoolScientific;
    /**
     * 信息安全软件，0：否，1：是
      */
    @Schema(description = "信息安全软件", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "信息安全软件不能为空")
    private Integer informationSecuritySoftware;
    /**
     * 是否采购机动车保险服务，0：否，1：是
      */
    @Schema(description = "是否采购机动车保险服务", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否采购机动车保险服务不能为空")
    private Integer isCarInsuranceServer;
    /**
     * 是否缴纳履约保证金，0：否，1：是
      */
    @Schema(description = "是否缴纳履约保证金", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isPerformanceMoney;
    /**
     * 是否存在预付款，0：否，1：是
      */
    @Schema(description = "是否存在预付款", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isPrepayment;
    /**
     * 是否缴纳质量保证金，0：否，1：是
      */
    @Schema(description = "是否缴纳质量保证金", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isRetentionMoney;
    /***
     * 是否涉密采购(1:是,0:否)
     */
    @Schema(description = "是否涉密采购", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否涉密采购不能为空")
    private Integer secret;
    /**
     * 采购人传真
      */
    @Schema(description = "采购人传真", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orgFax;
    /**
     * 采购单位名称
      */
    @Schema(description = "采购单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购单位名称不能为空")
    private String buyerOrgName;

    /**
     * 履约地点
      */
    @Schema(description = "履约地点", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "履约地点不能为空")
    private String performAddress;
    /**
     * 履约结束时间
      */
    @Schema(description = "履约结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "履约结束时间不能为空")
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date performEndDate;
    /**
     * 履约开始时间
      */
    @Schema(description = "履约开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "履约开始时间不能为空")
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date performStartDate;

    /**
     * 供应商联系地址
      */
    @Schema(description = "供应商联系地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商联系地址不能为空")
    private String registeredAddress;
    /**
     * 是否专门面向中小企业采购，0：否，1：是
      */
    @Schema(description = "是否专门面向中小企业采购", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否专门面向中小企业采购不能为空")
    private Integer reserveStatus;
    /**
     * 合同金额大写
      */
    @Schema(description = "合同金额大写", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "合同金额大写不能为空")
    private String shiftMoney;
    /**
     * 签署地点
      */
    @Schema(description = "签署地点", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractSignAddress;
    /**
     * 签署时间
      */
    @JsonFormat( pattern = "yyyy-MM-dd")
    @Schema(description = "签署时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "签署时间不能为空")
    private Date contractSignTime;
    /**
     * 供应商传真
      */
    @Schema(description = "供应商传真", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierFax;
    /**
     * 供应商特殊性质
      */
    @Schema(description = "供应商特殊性质", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "供应商特殊性质不能为空")
    private String supplierFeatures;

    /**
     * 供应商联系电话
      */
    @Schema(description = "供应商联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商联系电话不能为空")
    private String supplierLinkMobile;
    /**
     * 供应商所在区域
      */
    @Schema(description = "供应商所在区域", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "供应商所在区域不能为空")
    private String supplierLocation;
    /**
     * 供应商代表人
      */
    @Schema(description = "供应商代表人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商代表人不能为空")
    private String supplierProxy;
    /**
     * 供应商拥有者性别
      */
    @Schema(description = "供应商拥有者性别", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商拥有者性别不能为空")
    private String supplierSex;
    /**
     * 供应商规模
      */
    @Schema(description = "供应商规模", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "供应商规模不能为空")
    private String supplierSize;
    /**
     * 合同总金额
      */
    @Schema(description = "合同总金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同总金额不能为空")
    private Double totalMoney;
    /**
     * 计划名称
     */
    @Schema(description = "计划名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划名称不能为空")
    private String buyPlanName;

    /**
     * 履约保证金金额
     */
    @Schema(description = "履约保证金金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double performanceMoney;
    /**
     * 履约保证金缴纳方式
     */
    @Schema(description = "履约保证金缴纳方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String performanceMoneyType;
    /**
     * 质量保证金金额
     */
    @Schema(description = "质量保证金金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double retentionMoney;
    /**
     * 质量保证金缴纳方式
     */
    @Schema(description = "质量保证金缴纳方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String retentionMoneyType;
    /**
     * 外资国别类型
     */
    @Schema(description = "外资国别类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String countryType;
    /**
     * 采购人区划编码
     */
    @Schema(description = "采购人区划编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购人区划编码不能为空")
    private String regionCode;
    /**
     * 采购人区划名称
     */
    @Schema(description = "采购人区划名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotBlank(message = "采购人区划名称不能为空")
    private String regionName;
    /**
     * 采购计划备案书/标准书编号
     */
    @Schema(description = "采购计划备案书/标准书编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanCode;
}








