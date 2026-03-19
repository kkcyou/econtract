package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class TemplateVersionVO {
    String templateId;
    List<BigDecimal> versionList;
}
