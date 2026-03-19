package com.yaoan.module.econtract.dal.dataobject.freezed;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_freezed_contract")
public class FreezedContractDO extends TenantBaseDO implements Serializable {
    private static final long serialVersionUID = -1307387662447868836L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 主合同id
     */
    private String contractId;
    /**
     * 申请类型  冻结 0  解冻 1
     */
    private Integer type;
    /**
     * 签署截止日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;

    /**
     * 文件地址id
     */
    private Long fileAddId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 流程id
     */
    private String processInstanceId;
}