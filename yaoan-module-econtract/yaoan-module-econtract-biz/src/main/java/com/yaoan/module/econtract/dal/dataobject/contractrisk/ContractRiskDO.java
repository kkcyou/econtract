package com.yaoan.module.econtract.dal.dataobject.contractrisk;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 合同风险 DO
 *
 * @author lls
 */
@TableName("ecms_contract_risk")
@KeySequence("ecms_contract_risk_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractRiskDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 274812596854720152L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同id
     */
    private String contractId;
    /**
     * 处理id 用于关联附件表 取处理附件
     */
    private String handleId;
    /**
     * 风险处理状态 未处理0 已处理1 
     */
    private Integer status;
    
    /**
     * 风险类型
     * {@link com.yaoan.module.econtract.enums.ContractStatusEnums}
     */
    private Integer riskType;
    /**
     * 风险原因/争议内容
     */
    private String riskReason;

    /**
     * 计划重新开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date restartDate;

    /**
     * 处理人
     */
    private String handleUser;
    /**
     * 处理日期
     */
    private LocalDateTime handleTime;
    /**
     * 处理结果
     */
    private String handleResult;
    /**
     * 处理后合同状态
     */
    private Integer handleResultStatus;
  

}