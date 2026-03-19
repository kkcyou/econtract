package com.yaoan.module.bpm.api.task.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * 流程实例的取消 Request DTO
 *
 * @author doujl
 */
@Data
public class BpmProcessInstanceCancelReqDTO {

    @NotEmpty(message = "流程实例的编号不能为空")
    private String id;

    @NotEmpty(message = "取消原因不能为空")
    private String reason;
}
