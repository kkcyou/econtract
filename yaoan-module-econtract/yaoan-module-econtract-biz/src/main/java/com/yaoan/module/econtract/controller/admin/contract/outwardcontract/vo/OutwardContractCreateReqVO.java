package com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Pele
 */
@Schema(description = "管理后台 - 对外合同创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OutwardContractCreateReqVO extends OutwardContractBaseVO {

}
