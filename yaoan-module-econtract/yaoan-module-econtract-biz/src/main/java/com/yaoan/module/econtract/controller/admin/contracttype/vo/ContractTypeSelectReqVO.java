package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/13 11:55
 */
@Data
public class ContractTypeSelectReqVO {
    private Integer categoryId;
    /**
     * 1查的是启用的
     * 0查的是全部的
     * */
    private Integer flag;

    /**
     * 0=不展示政采的
     * 1=只展示政采的
     * （不传参默认0）
     * */
    private Integer isGov;

}
