package com.yaoan.module.econtract.api.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author doujiale
 */
@Data
public class DemoRespDTO {

    private static final long serialVersionUID = 2988917403040867754L;

    @Schema(name = "name", title = "姓名")
    @NotBlank(message = "name参数不能为空")
    private String name;
}