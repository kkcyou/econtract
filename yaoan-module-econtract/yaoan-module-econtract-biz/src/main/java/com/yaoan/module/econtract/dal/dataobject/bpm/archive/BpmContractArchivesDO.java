package com.yaoan.module.econtract.dal.dataobject.bpm.archive;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;

/**
 * 合同档案工作流表
 */
@Data
@TableName("ecms_bpm_contract_archives")
public class BpmContractArchivesDO extends DeptBaseDO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 流程实例的编号
     */
    private String processInstanceId; // 流程实例的编号
    /**
     * 申请结果（1=处理中 2=通过 3=不通过 4=已取消 5=退回）
     */
    private int result;
    /**
     * 归档id
     */
    private String archiveId;
    /**
     * 单据类型 归档0 补充1
     */
    private int type;
    /**
     * 补充备注
     */
    private String remark;
    /**
     * 创建人名称
     */
    private String creatorName;
}
