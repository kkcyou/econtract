package com.yaoan.module.system.controller.admin.gcy.supplier.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * SupplierInfoVO
 * @author doujiale
 */
@Data
@Schema(description = "管理后台 - 供应商列表 RespVO")
public class SupplierInfoVo {

    private String id;
    /**
     * 地址
     */
    private String legalAddr;
    /**
     * 电话
     */
    private String legalTel;
    /**
     * 法人姓名
     */
    private String legalPerson;
    /**
     * 信用代码
     */
    private String orgCode;
    /**
     * 供应商名称
     */
    private String supplyCn;
    /**
     * 联系人地址
     */
    private String personAddr;
    /**
     * 联系人电话
     */
    private String personMobile;
    /**
     * 联系人姓名
     */
    private String personName;
    /**
     * 工商注册地址（地区字典+地址）
     */
    private String regAddr;
    /**
     * 企业规模（大、中、小、微、其他）(提示：填写''营业收入''和''从业人员''信息后，系统会自动做出企业规模认定。)0 大型企业\n4 微型企业\n1 中型企业\n2
     * 小型企业\n3 其他
     */
    private String unitScopeCode;
    /**
     * 开户银行账号
     */
    private String bankAccount;
    /**
     * 开户银行
     */
    private String bankName;
}
