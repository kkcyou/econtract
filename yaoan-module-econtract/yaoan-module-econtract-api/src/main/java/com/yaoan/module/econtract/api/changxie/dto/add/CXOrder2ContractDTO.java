package com.yaoan.module.econtract.api.changxie.dto.add;

import lombok.Data;

import java.util.Date;

/**
 * @description: 订单信息回填合同文件DTO（畅写服务）
 * @author: Pele
 * @date: 2024/4/26 13:41
 */
@Data
public class CXOrder2ContractDTO {

    private String projectName;

    private String packageName;

    private String packageSort;

    private String code;

    private String partAName;

    private String partBName;

    private String partAAddr;


    private String partBAddr;

    private Date signDate;
}
