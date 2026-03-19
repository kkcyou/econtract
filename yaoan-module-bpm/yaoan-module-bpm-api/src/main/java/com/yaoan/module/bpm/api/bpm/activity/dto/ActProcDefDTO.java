package com.yaoan.module.bpm.api.bpm.activity.dto;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/1 21:02
 */
@Data
public class ActProcDefDTO {
    private String id;
    private Integer rev;
    private String category;
    private String name;
    private String key;
    private Integer version;
    private String deploymentId;
}
