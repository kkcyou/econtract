package com.yaoan.module.econtract.controller.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ModelIdAndNameVO {

    /**
     * 模板id
     */
    private String id;

    /**
     * 模板名称
     */
    private String name;

}
