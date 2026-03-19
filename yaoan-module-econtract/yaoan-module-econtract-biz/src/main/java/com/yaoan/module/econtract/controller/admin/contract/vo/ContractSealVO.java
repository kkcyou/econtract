package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

/**
 * 合同章信息
 */
@Data
public class ContractSealVO {
    /**
     * 角色 0甲方 1乙方
     */
    private Integer role;

    /**
     * 合同章类型 0签名章 1骑缝章 2合同章
     */
    private Integer type;

    /**
     * 章位置
     */
    private String position;

    /**
     * 章比例
     */
    private String ratio;

    /**
     * 页码
     */
    private Long pageNumber;
}
