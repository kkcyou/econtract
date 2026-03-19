package com.yaoan.module.econtract.controller.admin.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 17:18
 */
@Data
public class ModelVO {
    /** 模板id */
    private String id;

    /** 模板编码 */
    private String code;

    /** 模板名称 */
    private String name;

    /** 模板分类ID */
    private Integer categoryId;

    /** 合同类型 */
    private String contractType;

    /** 时效模式 */
    private Integer timeEffectModel;

    /** 模板生效时间 */
    private Date effectStartTime;

    /** 模板失效时间 */
    private Date effectEndTime;

    /** 模板描述 */
    private String content;

    /** 模板文件，存放的是文件路径 */
    private String modelFilePath;

    /** 模板文件，文件base64数据 */
    private String fileBase64;

    /** 模板状态 */
    private Integer status;

    /** 审批状态（0=未审核，1=通过，2=不通过,，3=已审批） */
    private Integer approveStatus;
}
