package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteContractRequestDTO  implements Serializable {
    private String contractGuid;
    private String platform;
}