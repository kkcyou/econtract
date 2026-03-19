package com.yaoan.module.infra.api.file.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class FileDTO {

    private Long id;
    /**
     * 配置编号
     *
     */
    private Long configId;
    /**
     * 文件路径
     */
    private String path;

    /**
     * 原文件名
     */
    private String name;
    /**
     * 文件 URL
     */
    private String url;
    /**
     * 文件MIME类型
     */
    private String type;
    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 页数
     */
    private Integer pageCount;

    /**
     * 创建者
     */
    private Long creator;

    private LocalDateTime updateTime;

    private String bucketName;

}
