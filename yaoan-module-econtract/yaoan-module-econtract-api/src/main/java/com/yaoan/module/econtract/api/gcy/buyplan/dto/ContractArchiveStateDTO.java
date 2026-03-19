package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractArchiveStateDTO implements Serializable {

    private static final long serialVersionUID = 2984263968140354247L;

    private String contractGuid;
    private String platform;

}
