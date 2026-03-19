package com.yaoan.module.econtract.dal.dataobject.gcy.rel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/12 13:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_order_rel")
public class ContractOrderRelDO extends BaseDO {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 合同id
     */
    private String contractId;

}
