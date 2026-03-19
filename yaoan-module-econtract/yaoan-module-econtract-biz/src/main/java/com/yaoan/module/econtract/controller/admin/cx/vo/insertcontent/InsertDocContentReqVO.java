package com.yaoan.module.econtract.controller.admin.cx.vo.insertcontent;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/28 16:10
 */
@Data
public class InsertDocContentReqVO {

    /**
     * 待处理的文档路径必填
     */
    private String fileUrl;

    /**
     * 必填
     * 0:内容来自word
     * 1:内容来自excel
     */
    private Integer contentType;

    /**
     * 必填要插入文档的内容，由后端提取接口提供
     */
    private String txtUrl;

    /**
     * 插入文档的指定内容域标签名称,选填 不填则默认插入文档末尾
     */
    private String contentControlName;

    /**
     * 是否清空内容域，默认为false
     */
    private String clear;

    /**
     * /回调Url，文档处理完成后通知的回调地址
     */
    private String callbackUrl;
}
