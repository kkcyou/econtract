package com.yaoan.module.econtract.api.purchasing.dto;

import lombok.Data;

/**
 * @description: 虚拟系统对接业务DTO
 * @author: Pele
 * @date: 2023/11/13 10:56
 */
@Data
public class BusinessInfoDTO {
    /**
     * 业务id
     */
    private String businessId;
    /**
     * 业务名称
     */
    private String businessName;

}
