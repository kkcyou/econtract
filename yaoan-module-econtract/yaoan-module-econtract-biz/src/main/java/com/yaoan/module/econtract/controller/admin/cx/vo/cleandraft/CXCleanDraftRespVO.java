package com.yaoan.module.econtract.controller.admin.cx.vo.cleandraft;

import lombok.Data;

import java.util.Map;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/18 15:36
 */
@Data
public class CXCleanDraftRespVO {

    private String key;

    private Map<String, String> urls;

    private Boolean end;
}
