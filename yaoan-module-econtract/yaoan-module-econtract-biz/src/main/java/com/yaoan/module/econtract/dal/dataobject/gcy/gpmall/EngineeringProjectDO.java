package com.yaoan.module.econtract.dal.dataobject.gcy.gpmall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 订单的工程项目信息
 * @author: Pele
 * @date: 2023/12/3 19:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_engineering_project")
public class EngineeringProjectDO extends BaseDO {

    private static final long serialVersionUID = 3736100192712680723L;
    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 承建单位名称
     */
    private String contractorName;

    /**
     * 项目编号
     */
    private String engineeringProjectCode;

    /**
     * 工程项目主键ID
     */
    private String engineeringProjectGuid;

    /**
     * 项目名称
     */
    private String engineeringProjectName;

    /**
     * 甲方单位
     */
    private String ownerOrgName;

    /**
     * 订单id
     */
    private String orderId;
}
