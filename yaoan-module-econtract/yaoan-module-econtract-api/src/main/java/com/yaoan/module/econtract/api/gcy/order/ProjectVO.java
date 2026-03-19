package com.yaoan.module.econtract.api.gcy.order;

import lombok.Data;

/**
 * @description: 订单的项目信息
 * @author: Pele
 * @date: 2023/12/3 19:25
 */
@Data
public class ProjectVO {

    private static final long serialVersionUID = 2801253594708030239L;
    /**
     * id主键
     */
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

}
