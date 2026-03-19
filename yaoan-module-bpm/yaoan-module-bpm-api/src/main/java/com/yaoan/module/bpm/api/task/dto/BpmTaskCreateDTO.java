package com.yaoan.module.bpm.api.task.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author doujiale
 */
@Data
@ToString(callSuper = true)
public class BpmTaskCreateDTO {

    private Long userId;

    private LocalDateTime createTime;

}
