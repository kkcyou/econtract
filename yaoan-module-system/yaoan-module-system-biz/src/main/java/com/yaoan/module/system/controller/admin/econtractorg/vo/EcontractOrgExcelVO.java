package com.yaoan.module.system.controller.admin.econtractorg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 电子合同单位信息 Excel VO
 *
 * @author admin
 */
@Data
public class EcontractOrgExcelVO {

    @ExcelProperty("单位id")
    private String id;

    @ExcelProperty("单位名称")
    private String name;

    @ExcelProperty("单位地址")
    private String address;

    @ExcelProperty("纳税人识别号")
    private String taxpayerId;

    @ExcelProperty("联系传真")
    private String linkFax;

    @ExcelProperty("联系人")
    private String linkMan;

    @ExcelProperty("联系电话")
    private String linkPhone;

    @ExcelProperty("开户银行账号")
    private String bankAccount;

    @ExcelProperty("开户名称")
    private String bankAccountName;

    @ExcelProperty("开户银行名称")
    private String bankName;

    @ExcelProperty("区划编码")
    private String regionCode;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("邮政编码")
    private String postCode;

    @ExcelProperty("法人名称")
    private String legal;

    @ExcelProperty("法人电话")
    private String legalPhone;

    @ExcelProperty("区划guid")
    private String regionGuid;

}
