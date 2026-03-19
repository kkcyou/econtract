package com.yaoan.module.econtract.controller.admin.catalog.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ModelIdVO implements Serializable {
    private String modelName;
    private String modelCode;
    private String modelId;
}
