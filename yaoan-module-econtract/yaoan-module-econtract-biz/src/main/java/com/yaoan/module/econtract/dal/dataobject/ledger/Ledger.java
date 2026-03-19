package com.yaoan.module.econtract.dal.dataobject.ledger;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Pele
 * 台账表
 * @TableName ecms_ledger
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_ledger")
public class Ledger extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = -9108801779465914866L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同编号
     */
    private String code;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 合同类型（all=全部、sales=销售合同、purchase=采购合同、rental =租赁合同、transportation =运输合同、warehousing =仓储合同 等）
     */
    private String contractType;

    /**
     * 合同状态（all=全部，signing=签署中，signed=签署完成，unsigned=签署未完成，overdue_unsigned=逾期未签署、voiding=作废中、voided=已作废、terminating=终止中、terminated=已终止、filed=已归档、fufilling=履约中、overdue_fufilling=履约超期、pause_fulfil=履约暂停、finished=合同完成、overdue_finished=合同超期完成）
     */
    private Integer contractStatus;

    /**
     * 合同金额
     */
    private Double contractFinance;

//    /**
//     * 我方签约主体
//     */
//    private String myContractParty;

    /**
     * 相对方签约主体
     */
    private String counterparty;

    /**
     * 合同签订时间
     */
    private LocalDateTime signTime;

    /**
     * 合同归档时间
     */
    private LocalDateTime filingTime;

    /**
     * 合同签署方式（all=全部，scan=扫码签、ukey=Ukey签署）
     */
    private String signType;

//    /**
//     * 合同用章类型(all=全部，contract =合同章、official =公章、corporate =法人章
//     */
//    private String sealingType;





}