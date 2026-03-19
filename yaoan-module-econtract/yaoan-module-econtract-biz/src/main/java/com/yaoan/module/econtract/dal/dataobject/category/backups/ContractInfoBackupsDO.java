package com.yaoan.module.econtract.dal.dataobject.category.backups;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @Description: 合同备份表
 * @Version: 1.0
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_backups")
public class ContractInfoBackupsDO extends BaseDO {

    private static final long serialVersionUID = -6461939296309909328L;

//    /**
//     * 主键
//     */
//    @TableId(value = "id", type = IdType.ASSIGN_UUID)
//    private String id;

    /**
     * 订单id
     */
    @TableId
    private String orderId;

    /**
     * 合同草拟备份数据字符串
     */
    private String contractInfo;




}
