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
@TableName("ecms_document_rel")
public class DocumentRelDO extends TenantBaseDO implements Serializable {
    private static final long serialVersionUID = 3817038895658343854L;
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
    private String name;

    /**
     * 附件地址id
     */
    private Long addId;

    /**
     * 备注
     */
    private String remark;
}
