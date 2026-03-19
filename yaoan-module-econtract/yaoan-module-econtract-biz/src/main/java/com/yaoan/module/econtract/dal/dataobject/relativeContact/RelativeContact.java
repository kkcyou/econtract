package com.yaoan.module.econtract.dal.dataobject.relativeContact;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.econtract.enums.saas.RealNameEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author lls
 * @since 2024-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_relative_contact")
public class RelativeContact extends TenantBaseDO implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    private Long userId;
    @Schema(description = "相对方id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String relativeId;
    @Schema(description = "联系人名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "联系人部门", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String dept;
    @Schema(description = "联系人职务", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String duty;
    @Schema(description = "联系人邮箱", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String email;
    @Schema(description = "联系人手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactTel;
    @Schema(description = "联系人证件类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cardType;
    @Schema(description = "联系人证件号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cardNo;
    @Schema(description = "公司id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long companyId;
    @Schema(description = "部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long deptId;

    /**
     * {@link RealNameEnums}
     * */
    @Schema(description = "实名情况", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer realName;

}
