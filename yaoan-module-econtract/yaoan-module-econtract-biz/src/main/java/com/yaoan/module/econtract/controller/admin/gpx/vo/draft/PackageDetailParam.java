package com.yaoan.module.econtract.controller.admin.gpx.vo.draft;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/29 15:03
 */
@Data
public class PackageDetailParam {
    ;
    /**
     * 主键id
     */
    private String id;
    /**
     * 明细ID
     */
    private String detailId;
    /**
     * 包id
     */
    private String packageId;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 序号
     */
    private Integer paramNum;
    /**
     * 自定义序号
     */
    private String paramNumStr;
    /**
     * 明细名称
     */
    private String deatilName;
    /**
     * 加星参数 0/1 否/是
     */
    private Integer parameterStar;
    /**
     * 技术参数
     */
    private String detailParameter;
}
