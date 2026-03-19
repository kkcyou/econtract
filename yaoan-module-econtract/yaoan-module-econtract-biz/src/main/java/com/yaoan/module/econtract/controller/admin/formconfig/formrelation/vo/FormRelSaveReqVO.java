package com.yaoan.module.econtract.controller.admin.formconfig.formrelation.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/20 18:02
 */
@Data
public class FormRelSaveReqVO {


    private String businessId;

    private List<FormInfoReqVO> formList;


}
