package com.yaoan.module.econtract.dal.dataobject.relative;

import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.econtract.enums.relative.RelativeStatusV2Enums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lls
 * @since 2024-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_relative")
public class Relative extends TenantBaseDO implements Serializable {
    private static final long serialVersionUID = 8124254919406366222L;
    /**
     * 相对方id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "相对方编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    //相对方名称
    @Schema(description = "相对方名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "是否仅本租户使用，0 是 1否")
    private Integer tenantType;
    
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
    //相对方状态 黑名单状态 正常0 ，移入中1 ，黑名单2，移出中3, 草稿4, 审批中5
    /**
     * {@link RelativeStatusV2Enums}
     * */
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
     * 流程实例id
     * */
    private String processInstanceId;

    /**
     * 流程状态（0=草稿箱，null=我得提交，2=已通过）
     * {@link com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum}
     * */
    private Integer result;


    /**
     * 提交时间
     * */
    private LocalDateTime submitTime;

    /**
     * 审批时间
     * */
    private LocalDateTime approveTime;

    public String getCompanyName(){
        return this.getName();
    }

    public String getCreditCode(){
        return this.getCardNo();
    }

    public String getAccount(){
        return this.getBankAccount();
    }

    public String getIdCard(){
        return this.getCardNo();
    }
    public String getAccountStatus(){
        return this.getStatus();
    }


    /**
     * 激活状态
     * 0=未激活
     * 1=已激活
     * */
    private Integer active;









}
