package com.yaoan.module.bpm.dal.dataobject.act;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("act_re_model")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActReModel {

    @TableId(value = "ID_", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField(value = "REV_")
    private Integer rev;

    @TableField(value = "NAME_")
    private String name;

    @TableField(value = "KEY_")
    private String key;

    @TableField(value = "CATEGORY_")
    private String category;

    @TableField(value = "CREATE_TIME_")
    private Date createTime;

    @TableField(value = "LAST_UPDATE_TIME_")
    private Date lastUpdateTime;

    @TableField(value = "VERSION_")
    private Integer version;

    @TableField(value = "META_INFO_")
    private String metaInfo;

    @TableField(value = "DEPLOYMENT_ID_")
    private String deploymentId;

    @TableField(value = "EDITOR_SOURCE_VALUE_ID_")
    private String editorSourceValueId;

    @TableField(value = "EDITOR_SOURCE_EXTRA_VALUE_ID_")
    private String editorSourceExtraValueId;

    @TableField(value = "TENANT_ID_")
    private String tenantId;
}
