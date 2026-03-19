package com.yaoan.module.econtract.controller.admin.cx.vo.converter;

import com.yaoan.module.econtract.enums.changxie.FileTypeEnums;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 16:01
 */
@Data
public class CXFileConverterReqVO {

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
    /**
     * 设置请求是否同步执行，默认同步。
     * 支持的值: true, false
     * 默认同步 false
     */
    private Boolean async;

    /**
     * 回调函数，用于异步处理文档，async参数不传或传false起效
     */
    private String callbackUrl;

    /**
     * 设置文件编码， 当从csv或txt格式 转换时 支持值 65001 (UTF-8)
     */
    private Integer codePage;

    /**
     * 设置转换CSV格式 时的分隔符
     * 支持的值：
     * 0 - 无隔符
     * 1 - tab
     * 2 - 分号
     * 3 - 冒号
     * 4 - 逗号
     * 5 - 空格
     */
    private Integer delimiter;

    /**
     * 转换后的文档名称
     */
    private String title;

    /**
     * 当outputtype类型 为 bmp、gif、jpg、png格式时，可设置如下参数：
     * aspect 设置图像适应模式：
     * 支持的值：0 拉伸适应高 度 和 宽 度 1 保持图像宽高 比
     * first 设置文档 第一页生成缩 略图，还是所 有页生成缩略 图 。 值 为 false，则将所 有页生成缩略 图 并 打 包 成 zip包。默认值 为true
     * height 缩略图 的高度（以像 素为单位，默 认值：100）
     * width 缩略图的 宽度（以像素 为单位，默认值：100）
     */
    private Thumbnail thumbnail;

    /**
     * 设置源文档的密码，如果源文档存在密码
     */
    private String password;

    /**
     * 设置转换电子表格到PDF参数
     */
    private SpreadsheetLayout spreadsheetLayout;

    /**
     * 开启畅写在线 token
     */
    private String token;

    @Data
    public static class Thumbnail {
        private Integer aspect;
        private Boolean first;
        private Integer height;
        private Integer width;
    }

    @Data
    public static class SpreadsheetLayout {
        private Integer fitToHeight;
        private Integer fitToWidth;
        private Boolean gridLines;
        private Boolean headings;
        private Boolean ignorePrintArea;
        private String orientation;
        private Integer scale;
        private PageSize pageSize;
        private Margins margins;
    }

    @Data
    public static class PageSize {
        private String height;
        private String width;
    }

    @Data
    public static class Margins {
        private String bottom;
        private String left;
        private String right;
        private String top;
    }
}
