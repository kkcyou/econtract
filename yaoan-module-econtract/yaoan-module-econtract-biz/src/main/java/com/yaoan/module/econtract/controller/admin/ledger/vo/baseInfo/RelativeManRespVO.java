package com.yaoan.module.econtract.controller.admin.ledger.vo.baseInfo;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/19 21:46
 */
@Data
public class RelativeManRespVO {

    /**
     * 相对方单位名称
     */
    private String deptName;

    /**
     * 相对方联系人名称
     */
    private String name;

    /**
     * 相对方联系人电话
     */
    private String tel;
}
