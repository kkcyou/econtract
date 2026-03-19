package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data

@TableName("ecms_sppgpt_contract_details")
public class ContractDetailsDO implements Serializable {

    private static final long serialVersionUID = 7351708843983801476L;

    /**
     * 任务编号
     */
    @TableId(value = "task_id", type = IdType.AUTO)
    private String id;
    /**
     *任务状态，0:解析中，1:已完成
     */
    private String status;
    /**
     * 合同基本信息
     */
    private String contractBaseInfo;
    /**
     * 采购明细
     */
    private String purchaseDetail;
    /**
     * 合同供应商
     */
    private String suppliers;
    /**
     * 合同转让/分包供应商
     */
    private String subSuppliers;
    /**
     * 支付计划
     */
    private String paymentPlan;
    /**
     * 合同履约验收要求
     */
    private String acceptRequirement;
    /**
     * 项目经理
     */
    private String projectManager;
}
