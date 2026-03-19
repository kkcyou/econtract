package com.yaoan.module.econtract.controller.admin.foxi.vo.save;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025-9-5 16:29
 */
@Data
public class FoxiSaveReqVO {

    /** 是当前回存文档的ID */
    private String docId;
    private String action = "saveBack";
    private FoxiSaveDataReqVO data;

}


