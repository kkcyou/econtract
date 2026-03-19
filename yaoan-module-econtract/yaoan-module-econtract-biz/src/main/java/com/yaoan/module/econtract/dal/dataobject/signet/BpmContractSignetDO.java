package com.yaoan.module.econtract.dal.dataobject.signet;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;



@Data
@TableName("ecms_bpm_contract_signet")
@EqualsAndHashCode(callSuper = true)
public class BpmContractSignetDO extends DeptBaseDO {
    private static final long serialVersionUID = -6994848868644744730L;

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 印章id
     */
    private String signetId;

    /**
     * 印章名称
     */
    private String signetName;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 业务编号
     */
    private String businessCode;
    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 申请结果
     */
    private Integer result;

    /**
     * 申请事由
     */
    private String reason;

    /**
     * 公司id
     */
    private Long companyId;



}
