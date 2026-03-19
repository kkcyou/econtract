package com.yaoan.module.bpm.api.bpm.activity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ActReModelDTO {
    private String id;
    private Integer rev;
    private String name;
    private String key;
    private String category;
    private Date createTime;
    private Date lastUpdateTime;
    private Integer version;
    private String metaInfo;
    private String deploymentId;
    private String editorSourceValueId;
    private String editorSourceExtraValueId;
    private String tenantId;
}
