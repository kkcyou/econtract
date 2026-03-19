package com.yaoan.module.system.api.tenant.dto;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import lombok.Data;

/**
 * 租户 Response DTO
 *
 * @author doujl
 */
@Data
public class TenantRespDTO {

    private Long id;
    /**
     * 租户名，唯一
     */
    private String name;
    /**
     * 联系人的用户编号
     */
    private Long contactUserId;
    /**
     * 联系人
     */
    private String contactName;

}
