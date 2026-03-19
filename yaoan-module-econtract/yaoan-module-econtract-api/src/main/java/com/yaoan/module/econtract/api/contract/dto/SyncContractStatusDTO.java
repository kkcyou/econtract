package com.yaoan.module.econtract.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SyncContractStatusDTO {



    private List<ContractStatusDTO> contractList;


}
