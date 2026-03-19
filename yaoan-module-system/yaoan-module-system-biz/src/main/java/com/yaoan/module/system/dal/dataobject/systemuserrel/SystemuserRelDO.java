package com.yaoan.module.system.dal.dataobject.systemuserrel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 系统对接用户关系 DO
 *
 * @author lls
 */
@TableName("ecms_systemuser_rel")
@KeySequence("ecms_systemuser_rel_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemuserRelDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 采购单位id
     */
    private String buyerOrgId;
    /**
     * 采购人id
     */
    private String buyerUserId;
    /**
     * 对应本系统用户id
     */
    private Long currentUserId;
    /**
     * 对应本系统租户id
     */
    private Long currentTenantId;
    /**
     * 对应本系统公司id
     */
    private Long companyId;
    /**
     * 对应本系统部门id
     */
    private Long deptId;
    /**
     * 创建人名称
     */
    private String creatorName;

}
