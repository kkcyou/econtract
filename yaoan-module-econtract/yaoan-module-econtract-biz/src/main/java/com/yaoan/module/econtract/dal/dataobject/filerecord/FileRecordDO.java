package com.yaoan.module.econtract.dal.dataobject.filerecord;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: Pele
 * @date: 2024/7/4 16:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_file_record")
public class FileRecordDO extends TenantBaseDO {

    private static final long serialVersionUID = -8543390567528010400L;
    /** 主键 */
     private Long id;
    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 供应商id
     */
    private String supplierId;

    /**
     * 采购单位id
     */
    private String orgId;
}
