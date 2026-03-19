package com.yaoan.module.econtract.api.changxie.dto;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 16:43
 */
@Data
public class CXFileConverterRespDTO {

    /**
     * 转换是否完成
     */
    private Boolean endConvert;
    /**
     * 转换期间发生的错误。 可能的错误代码可以在这里找到
     */
    private Integer error;
    /**
     * 转换后文档的链接。 只有当endConvert参数设置为true时，才会接收到该参数
     */
    private String fileUrl;
    /**
     * 文件转换的百分比。 如果endConvert参数为true，则百分比等于100
     */
    private Integer percent;

}
