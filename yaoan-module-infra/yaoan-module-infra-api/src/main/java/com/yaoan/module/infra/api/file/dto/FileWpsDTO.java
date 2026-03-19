package com.yaoan.module.infra.api.file.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/31 15:54
 */
@Data
public class FileWpsDTO {

    /**
     * 编号，数据库自增
     */
    private Long id;

    /**
     * 原文件名
     */
    private String name;

    /**
     * 访问地址
     */
    private String url;


    /**
     * 访问地址
     */
    private String path;

    /**
     * 文件大小
     */
    private Integer size;


    /**
     * 文件MIME类型
     */
    private String type;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String creator;

    /**
     * 更新者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String updater;

    /**
     * 是否删除
     */
    private Boolean deleted;
}
