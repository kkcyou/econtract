package com.yaoan.module.econtract.controller.admin.contract.xmlvo;



import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * 项目经理信息
 */
@Data
@XmlRootElement(name = "ProjectManagerInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectManagerInformation {
    /**
     * 人员编号
     */
    private String projectManagerCode;

    /**
     * 人员名称
     */
    private String projectManagerName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 性别
     */
    private String sex;

    /**
     * 职称
     */
    private String title;

    /**
     * 住址
     */
    private String address;
}

