package com.yaoan.module.infra.dal.dataobject.file;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/17 18:01
 */

@TableName("infra_file")
@KeySequence("infra_file_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileFastDO  extends BaseDO {

    /**
     * 编号，数据库自增
     */
    private Long id;
    /**
     * 配置编号
     *
     * 关联 {@link FileConfigDO#getId()}
     */
    private Long configId;

    /**
     * 原文件名
     */
    private String name;

    /**
     * 文件的 MIME 类型，例如 "application/octet-stream"
     */
    private String type;


    /**
     * 文件页数
     */
    private Integer pageCount;
}
