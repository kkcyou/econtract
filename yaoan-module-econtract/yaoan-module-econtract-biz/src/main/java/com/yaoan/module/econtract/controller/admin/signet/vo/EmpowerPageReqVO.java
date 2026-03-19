package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;


@Data
@ToString(callSuper = true)
public class EmpowerPageReqVO extends PageParam {
    private static final long serialVersionUID = -5493092925389299136L;
    /**
     * 成员名称
     */
    @Schema(description = "成员名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     * 成员部门
     */
    @Schema(description = "成员部门", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String dept;
}
