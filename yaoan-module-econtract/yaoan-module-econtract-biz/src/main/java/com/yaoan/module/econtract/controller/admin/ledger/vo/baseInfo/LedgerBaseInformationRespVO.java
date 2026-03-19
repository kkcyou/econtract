package com.yaoan.module.econtract.controller.admin.ledger.vo.baseInfo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/19 17:32
 */
@Data
public class LedgerBaseInformationRespVO {

    /**
     * 合同编码
     */
    private String contractCode;

    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同类型
     */
    private String contractTypeStr;

    /**
     * 履约状态
     */
    private String contractStatus;

    /**
     * 我方签约主体str
     */
    private String myCompanyName;

//    /**
//     * 相对方方签约主体
//     */
//    private String yourCompanyName;

    /**
     * 我方联系人
     */
    private String myrCompanyManName;

    /**
     * 我方联系人电话
     */
    private String myCompanyManTel;

    /**
     * 相对方联系人list
     */
    private List<RelativeManRespVO> relativeManList;


    /**
     * 合同签订日期
     */
    private Date contractSignTime;

//    /**
//     * 合同金额(不显示)
//     */
//    private String contractMoney;
//
//    /**
//     * 签署方式(不显示)
//     */
//    private String contractSignType;
//
//    /**
//     * 合同用印类型(不显示)
//     */
//    private String contractSealType;


}
