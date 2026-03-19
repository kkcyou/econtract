package com.yaoan.module.econtract.controller.admin.outward.contract.VO;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class ContractParameterRespDO {
    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 类型
     */
    private String type;

    /**
     * 参数内容
     */
    private String value;
}
