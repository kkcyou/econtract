package com.yaoan.module.econtract.controller.admin.performance.log.vo;

import com.yaoan.module.econtract.enums.perform.log.PerformanceLogModuleNameEnums;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/9 16:11
 */
@Data
public class ContractPerfLogSaveReqVO {

    //操作人
    private Long userId;
    //操作人名
    private String userName;
    //操作模块
    /**
     * {@link PerformanceLogModuleNameEnums}
     * */
    private String moduleName;
    //日志所属业务主键id
    private String businessId;
    //业务主键id
    //example 对履约计划发起验收申请时，businessId为履约计划id billId为验收id
    private String billId;

    //操作类型
    private String operateName;
    //操作记录内容
    private String operateContent;

}
