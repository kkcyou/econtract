package com.yaoan.module.econtract.dal.dataobject.gcy.gpmall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 订单的项目信息---项目信息有可能为空，因为直购订单就没有项目信息
 * @author: Pele
 * @date: 2023/12/3 19:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_project")
public class ProjectDO extends BaseDO {

    private static final long serialVersionUID = 2801253594708030239L;
    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 项目id
     */
    private String projectGuid;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目所属分类（A、B、C）
     */
    private String projectCategoryCode;

    /**
     * 项目所属分类名称（货物、工程、服务）
     */
    private String projectCategoryName;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 订单id
     */
    private String orderId;

    //=======2024-02-18  新增=========
    /**
     * 采购组织形式
     */
    private String kind;
//    /**
//     * 采购实施形式
//     */
//    private String buyImplementationForm;
//    /**
//     * 项目经办人
//     */
//    private String projectManager;
//    /**
//     * 项目经办人电话
//     */
//    private String projectManagerTel;
    /**
     * 项目负责人(联系人)
     */
    private String projectLeader;
    /**
     * 项目负责人电话
     */
    private String projectLeaderTel;

}
