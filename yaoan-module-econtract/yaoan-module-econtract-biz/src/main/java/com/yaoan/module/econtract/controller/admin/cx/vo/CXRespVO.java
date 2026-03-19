package com.yaoan.module.econtract.controller.admin.cx.vo;

import lombok.Data;

/**
 * @description: 响应
 * @author: Pele
 * @date: 2024/4/25 10:26
 */
@Data
public class CXRespVO {

    /**
     * · 0 无错误
     * · 1 无此文档
     * · 2 回调保存URL不正确
     * · 3 未知错误
     * · 4 forceave 命令调用之前文档无修改
     * · 5 错误的命令
     * · 6 token 错误
     */
    private Integer error;

    /**
     * 文档的唯一标识
     */
    private String key;

}
