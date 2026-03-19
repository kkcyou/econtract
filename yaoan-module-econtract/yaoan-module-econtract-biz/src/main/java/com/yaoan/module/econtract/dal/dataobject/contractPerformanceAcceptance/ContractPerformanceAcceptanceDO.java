package com.yaoan.module.econtract.dal.dataobject.contractPerformanceAcceptance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 验收 DO
 *
 * @author lls
 */
@TableName("ecms_contract_performance_acceptance")
@KeySequence("ecms_contract_performance_acceptance_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractPerformanceAcceptanceDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 8044663089574819587L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 标题
     */
    private String title;
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 验收id用户关联附件
     */
    private String acceptanceId;
    /**
     * 验收开始时间
     */
    private LocalDate acceptanceStartTime;
    /**
     * 验收结束时间
     */
    private LocalDate acceptanceEndTime;
    /**
     * 验收负责人
     */
    private Long acceptanceUser;
    /**
     * 备注
     */
    private String remark;

    /**
     * 验收状态 申请0 验收通过1 验收不通过2
     */
    private Integer status;
    /**
     * 验收备注
     */
    private String acceptanceRemark;
   

}