package com.yaoan.module.econtract.controller.admin.change.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;


@Data
public class ElementRespVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 要素
     */
    private String element;

    /**
     * 要素名称
     */
    private String elementName;

    /**
     * 要素名称
     */
    private String elementType;



}
