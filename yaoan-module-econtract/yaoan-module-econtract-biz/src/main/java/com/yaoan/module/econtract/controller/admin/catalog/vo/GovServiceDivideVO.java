package com.yaoan.module.econtract.controller.admin.catalog.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class GovServiceDivideVO implements Serializable {
    private String govServiceDivideGuid;
    private String purCatalogName;
    private String purCatalogCode;
}
