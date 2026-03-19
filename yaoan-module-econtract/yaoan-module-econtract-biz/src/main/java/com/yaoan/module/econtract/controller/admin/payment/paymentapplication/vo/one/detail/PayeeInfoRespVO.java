package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail;

import lombok.Data;

/**
 * @description: 收款方信息
 * @author: Pele
 * @date: 2023/12/21 20:18
 */
@Data
public class PayeeInfoRespVO {

    /**
     * 名称
     */
    private String name;

    /**
     * 收款方银行账号
     */
    private String bankAccount;

    /**
     * 收款方开户行
     */
    private String bankName;

}
