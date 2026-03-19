package com.yaoan.module.system.dal.dataobject.econtractorg;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 电子合同单位信息 DO
 *
 * @author admin
 */
@TableName("system_econtract_org")
@KeySequence("system_econtract_org_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EcontractOrgDO extends BaseDO {

    /**
     * 单位id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 单位名称
     */
    private String name;
    /**
     * 单位地址
     */
    private String address;
    /**
     * 纳税人识别号
     */
    private String taxpayerId;
    /**
     * 联系传真
     */
    private String linkFax;
    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 联系电话
     */
    private String linkPhone;
    /**
     * 开户银行账号
     */
    private String bankAccount;
    /**
     * 开户名称
     */
    private String bankAccountName;
    /**
     * 开户银行名称
     */
    private String bankName;
    /**
     * 区划编码
     */
    private String regionCode;
    /**
     * 邮政编码
     */
    private String postCode;
    /**
     * 法人名称
     */
    private String legal;
    /**
     * 法人电话
     */
    private String legalPhone;
    /**
     * 区划guid
     */
    private String regionGuid;

}
