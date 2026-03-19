package com.yaoan.module.econtract.api.listen.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/19 17:54
 */
@Data
public class FileVersionEventDTO {
    /**
     * 业务id
     */
    @NotNull(message = "业务id不能为空")
    private String businessId;
    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 备注
     */
    private String remark;
    /**
     * 流程实例id
     */
    private String processInstanceId;

}
