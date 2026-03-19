package com.yaoan.module.econtract.dal.dataobject.offlinefile;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

/**
 * @description:
 * @author: Pele
 * @date: 2025/3/10 18:13
 */
@TableName(value = "ecms_offline_file", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfflineFileDO extends TenantBaseDO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 业务id
     *  合同
     */
    private String businessId;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 业务类型
     * 1 = 合同
     */
    private Integer businessType;

}
