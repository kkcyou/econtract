package com.yaoan.module.econtract.controller.admin.cx.vo.add;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 17:50
 */
@Data
public class CXAddJsonDTO {

    private String name;

    private String id;

    /**
     * 是否清空内容域
     */
    private String clear;

    /**
     * 富文本
     */
    private String content;

}
