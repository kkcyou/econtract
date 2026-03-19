package com.yaoan.module.econtract.dal.dataobject.relativeBlacklist;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
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
@TableName("ecms_relative_blacklist_apply")
public class RelativeBlacklist extends TenantBaseDO implements Serializable {
    private static final long serialVersionUID = 8124254919406366222L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "相对方id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String relativeId;

    @Schema(description = "附件id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long fileId;

    @Schema(description = "附件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;
    
    @Schema(description = "申请状态 申请0  审批1 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer applyStatus;

    @Schema(description = "申请类型 移入0 移出1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer applyType;
    
    @Schema(description = "申请意见", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String applyMsg;

    @Schema(description = "审批类型 审批0 拒绝1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer auditType;

    @Schema(description = "审批意见", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String auditMsg;

}
