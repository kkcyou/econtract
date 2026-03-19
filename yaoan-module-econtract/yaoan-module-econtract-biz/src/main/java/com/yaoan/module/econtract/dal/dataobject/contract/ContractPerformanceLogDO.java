package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

import java.io.Serializable;

/**
 * @author lls
 * @date 2024-09-26
 * @desc 业务日志-履约日志
 */
@TableName("ecms_business_log")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractPerformanceLogDO extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = 6530039110425654783L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    //操作人
    private Long userId;
    //操作人名
    private String userName;
    //操作模块
    /**
     * PerformanceLogModuleNameEnums
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
