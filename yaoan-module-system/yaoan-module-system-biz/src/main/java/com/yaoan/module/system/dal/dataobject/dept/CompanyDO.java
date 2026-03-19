package com.yaoan.module.system.dal.dataobject.dept;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.enums.dept.MajorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公司表
 *
 * @author doujl
 */
@TableName("system_company")
@KeySequence("system_company_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyDO extends TenantBaseDO {

    /**
     * 单位ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 单位名称
     */
    private String name;
    /**
     * 单位ID
     */
//    private Long deptId;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 负责人
     * <p>
     * 关联 {@link AdminUserDO#getId()}
     */
    private Long leaderUserId;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 单位状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 单位类型
     * <p>
     * 枚举 {@link MajorEnum}
     */
    private Integer major;

    /**
     * 信用代码
     */
    private String creditCode;

    /**
     * 是否是供应商
     */
    private Integer supplier;

    /**
     * 父类id
     */
    private Long parentId;

    /**
     * 相对方id
     */
    private String relativeId;

}
