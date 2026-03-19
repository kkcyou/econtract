package com.yaoan.module.econtract.dal.dataobject.contract.version;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 合同版本
 * @author: Pele
 * @date: 2024/3/29 11:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_version")
public class ContractVersionDO extends DeptBaseDO {

    private static final long serialVersionUID = -2987573691608644120L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同
     */
    @TableField("contract_id")
    private String contractId;

    /**
     * 文件id
     */
    @TableField("file_id")
    private String fileId;

    /**
     * 文件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 版本号
     */
    @TableField("version_number")
    private Integer versionNumber;
}
