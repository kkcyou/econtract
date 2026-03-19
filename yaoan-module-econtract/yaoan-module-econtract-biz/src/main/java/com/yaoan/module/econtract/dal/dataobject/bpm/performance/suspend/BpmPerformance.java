package com.yaoan.module.econtract.dal.dataobject.bpm.performance.suspend;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 履约中止申请表
 * </p>
 *
 * @author doujiale
 * @since 2023-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_bpm_performance")
public class BpmPerformance extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 6958808125249625601L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 申请人的用户编号
     */
    private Long userId;

    /**
     * 履约标识
     */
    private String performanceId;

    /**
     * 申请类型
     */
    private String approveType;

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
    private String contractType;

    /**
     * 中止原因
     */
    private String reason;

    /**
     * 中止结果
     */
    private Integer result;

    /**
     * 流程实例的编号
     */
    private String processInstanceId;

    /**
     * 租户编号
     */
    private Long tenantId;

}
