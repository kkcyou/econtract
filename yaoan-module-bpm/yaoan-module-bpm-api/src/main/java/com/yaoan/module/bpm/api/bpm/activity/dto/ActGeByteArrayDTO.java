package com.yaoan.module.bpm.api.bpm.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Data
public class ActGeByteArrayDTO {

    private String id;

    private Integer rev;

    private String name;

    private String deploymentId;

    private byte[] bytes;

    private byte[] generated;
}
