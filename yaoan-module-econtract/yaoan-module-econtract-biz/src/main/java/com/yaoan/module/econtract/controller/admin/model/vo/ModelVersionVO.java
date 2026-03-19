package com.yaoan.module.econtract.controller.admin.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ModelVersionVO {
    String modelId;
    List<BigDecimal> versionList;
}
