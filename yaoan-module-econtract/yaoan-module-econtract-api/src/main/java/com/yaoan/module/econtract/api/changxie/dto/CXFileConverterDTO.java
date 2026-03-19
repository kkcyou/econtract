package com.yaoan.module.econtract.api.changxie.dto;

import com.yaoan.module.econtract.enums.changxie.FileTypeEnums;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 16:42
 */
@Data
public class CXFileConverterDTO {
    /**
     * 待转换文档的文件格式
     * {@link FileTypeEnums}
     */
    private String filetype;

    /**
     * 文档唯一标识
     */
    private String key;

    /**
     * 转换目标文档类型
     */
    private String outputtype;

    /**
     * 待转换源文档绝对url地址
     */
    private String url;
}
