package com.yaoan.module.econtract.dal.dataobject.term;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2025-5-8 19:24
 */
@Data
@TableName("ecms_term")
@Document(indexName = "es_term")
public class SimpleTerm {

    /**
     * 主键uuid
     * */
    @Field(type= FieldType.Keyword)
    private String id;

    /**
     * 条款名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    /**
     * 条款内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String termContentTxt;

    /**
     * 0 = 禁用
     * 1 = 启用
     * */
    @Field(type = FieldType.Integer)
    private Integer status;

    /**
     * 条款库类别 0公共库，1单位库，2其他
     */
    @Field(type = FieldType.Integer)
    private Integer termLibrary;

    @Field(type = FieldType.Date)
    private LocalDateTime updateTime;

    @Field(type = FieldType.Integer)
    private Long tenantId;
}