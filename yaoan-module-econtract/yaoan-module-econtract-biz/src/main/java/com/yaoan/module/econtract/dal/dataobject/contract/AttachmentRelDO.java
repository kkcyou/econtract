package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_attachment_rel")
public class AttachmentRelDO extends TenantBaseDO implements Serializable {


    private static final long serialVersionUID = -4707093093037343336L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 附件名称
     */
    private String attachmentName;

    /**
     * 附件地址id
     */
    private Long attachmentAddId;

    /**
     * 附件类型
     */
    private String attachmentType;

    /**
     * url
     */
    private String url;
}