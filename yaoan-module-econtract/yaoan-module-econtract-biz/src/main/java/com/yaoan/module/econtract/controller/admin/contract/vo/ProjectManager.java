package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

/**
 * 项目经理
 */
@Data
public class ProjectManager {
    /**
     * 姓名
     */
    private String managerName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idcard;

    /**
     * 职称
     */
    private String professionalTitle;

    /**
     * 证书名称
     */
    private String certificateName;

    /**
     * 证书编号
     */
    private String certificateNo;

}
