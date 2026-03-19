package com.yaoan.module.econtract.controller.admin.relative.vo;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RelativeByUserRespVO {
    /**
     * 相对方id
     */
    @Schema(description = "相对方id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;
    @Schema(description = "相对方userid", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
    @Schema(description = "主体类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String entityType;
    @Schema(description = "主体类型值", requiredMode = Schema.RequiredMode.REQUIRED)
    private String entityTypeName;

    @Schema(description = "单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String companyName;

    @Schema(description = "统一社会信用代码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creditCode;
    
    @Schema(description = "相对方编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    
    @Schema(description = "相对方名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "电子签约平台账号，主体为个人独有字段", requiredMode = Schema.RequiredMode.REQUIRED)
    private String account;

    /**
     * 联系人id
     */
    @Schema(description = "联系人id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long contactId;

    /**
     * 联系人电子签约平台账号,仅政府和单位有此字段
     */
    @Schema(description = "联系人电子签约平台账号,仅政府和单位有此字段", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contactAccount;

    @Schema(description = "个人手机号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String tel;

    @Schema(description = "企业单位手机号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contactTel;
    @Schema(description = "企业单位名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contactName;

    @Schema(description = "身份证号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String idCard;
    //flag=send 为发起方
    @Schema(description = "发起方标识", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String flag;

    @Schema(description = "传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fax;
    @Schema(description = "开户银行", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankName;
    @Schema(description = "银行账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankAccount;
    @Schema(description = "开户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankAccountName;


    /**
     * 1. 甲方 2. 乙方 3. 丙方 4. 丁方
     * （旧数据为空，默认就是乙方）
     */
    @Schema(description = "签署方主体角色", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer type;

    /**
     * 是否在单位端注册
     *
     */
    @Schema(description = "是否在单位端注册", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean isRegister;

    @Schema(description = "虚拟id,为工作流分配使用", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long virtualId;
    /**
     * 签署顺序
     */
    private Integer sort;
}
