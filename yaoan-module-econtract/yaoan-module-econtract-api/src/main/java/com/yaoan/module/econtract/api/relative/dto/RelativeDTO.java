package com.yaoan.module.econtract.api.relative.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 单位 Response DTO
 *
 * @author doujiale
 */
@Data
public class RelativeDTO {

   
    private String id;

    @Schema(description = "相对方编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    //相对方名称
    @Schema(description = "相对方名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "相对方用户id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long contactId;
    @Schema(description = "相对方用户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactName;
    @Schema(description = "相对方用户编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactAccount;
    //相对方类型 供应商1 客户2
    @Schema(description = "相对方类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer relativeType;
    //相对方性质  企业2  个人：1  单位3
    @Schema(description = "相对方性质", requiredMode = Schema.RequiredMode.REQUIRED)
    private String entityType;
    //相对方来源 1新增 2导入
    @Schema(description = "相对方来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sourceType;
    @Schema(description = "相对方等级", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String levelNo;
    //相对方状态 黑名单状态 正常0 ，移入中1 ，黑名单2，移出中3
    @Schema(description = "相对方状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String status;
    @Schema(description = "证件类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer cardType;
    @Schema(description = "证件号码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String cardNo;
    @Schema(description = "法人名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String legalName;
    @Schema(description = "法人证件类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer legalCardType;
    @Schema(description = "法人证件号码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String legalCardNo;
    @Schema(description = "负责人姓名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String headName;
    @Schema(description = "负责人证件类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer headCardType;
    @Schema(description = "负责人证件号码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String headCardNo;
    @Schema(description = "开户名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankAccountName;

    @Schema(description = "银行账号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankAccount;
    @Schema(description = "开户行名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankName;
    @Schema(description = "网址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String website;
    @Schema(description = "所在区域", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String area;
    @Schema(description = "详细地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String address;
    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contactTel;
    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String email;
    @Schema(description = "传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fax;
    @Schema(description = "简介", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;
    @Schema(description = "相对方公司id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long relativeCompanyId;
    @Schema(description = "公司id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long companyId;
    @Schema(description = "相对方类别", requiredMode = Schema.RequiredMode.REQUIRED)
    private String categoryId;
    @Schema(description = "虚拟id,为工作流分配使用", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long virtualId;
    /**
     * 激活状态
     * 0=未激活
     * 1=已激活
     * */
    private Integer active;
    private String creator;
    private String updater;
    /**
     * 多租户编号
     */
    private Long tenantId;
}
