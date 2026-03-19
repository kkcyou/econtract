package com.yaoan.module.econtract.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 16:37
 */
@Data
public class ModelIdCodeRespVO{

    /**
     * 模板id
     */
    private String id;

    /**
     * 模板编码
     */
    private String code;
}
