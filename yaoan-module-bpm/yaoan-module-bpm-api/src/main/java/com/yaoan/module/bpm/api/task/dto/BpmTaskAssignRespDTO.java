package com.yaoan.module.bpm.api.task.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
public class BpmTaskAssignRespDTO {

    private String definitionKey;
    private Long userId;

}
