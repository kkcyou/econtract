package com.yaoan.module.econtract.controller.admin.param.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/6/5 14:08
 */
@Data
public class TermV2RespVO {
    /**
     * 合同模板条款id
     */
    private String termId;

    private String title;

    /**
     * 条款序号
     */
    private Integer termNum;

    private List<ParamByTermRespVO> paramByTermRespVOList;
}
