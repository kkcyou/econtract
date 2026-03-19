package com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance;

import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 合同履约是实体类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_perf")
public class ContractPerformanceDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 8703443217406077789L;
    /**
     * 合同履约id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同编码
     */
    private String contractCode;
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 签署完成时间
     */
    private Date signFinishTime;

    /**
     * 合同类型id
     */
    private String contractTypeId;

//    /**
//     * 合同履约状态编码
//     */
//    private Integer conPerfStatusCode;

    /**
     * 合同状态编码
     */
    private Integer contractStatus;

    /**
     * 最终履约时间
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date perfTime;
    /**
     * 合同有效期-开始时间
     */
    private Date validity0;

    /**
     * 合同有效期-结束时间
     */
    private Date validity1;

}
