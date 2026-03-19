package com.yaoan.module.econtract.controller.admin.annotation.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/7 10:36
 */
@Data
public class BigAnnotationListRespVO {

    /**
     * 角色名称
     */
    private String creatorRoleName;

    /**
     * 批注信息
     */
    private List<AnnotationListRespVO> respVOS;
}
