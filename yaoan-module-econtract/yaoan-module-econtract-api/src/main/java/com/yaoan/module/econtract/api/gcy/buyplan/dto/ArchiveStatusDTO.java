package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
public class ArchiveStatusDTO implements Serializable {


    private static final long serialVersionUID = -722258358879160361L;
    /**
     * 采购计划的唯一识别码(全局唯一)
     */
    private Integer historyId;


}
