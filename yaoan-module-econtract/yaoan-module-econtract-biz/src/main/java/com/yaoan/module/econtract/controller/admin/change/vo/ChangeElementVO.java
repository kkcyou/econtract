package com.yaoan.module.econtract.controller.admin.change.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;

import java.io.Serializable;


@Data
public class ChangeElementVO  {

    /**
     * 主键
     */
    private String id;

    /**
     * 变动id
     */
    private String changeId;

    /**
     * 要素id
     */
    private String elementId;
    /**
     * 要素名称
     */
    private String elementName;
    /**
     * 变更前名称
     */
    private String beforeName;

    /**
     * 变更后名称（简版）
     */
    private String afterName;

}
