package com.yaoan.module.econtract.api.gcy.order;

import com.yaoan.module.econtract.api.contract.dto.ContractDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 合同信息对象
 *
 * @author zhc
 * @since 2023-12-05
 */
@Data
public class ContractDataVo {
    /**
     * 合同信息对象列表
     */
    @Schema(description = "合同信息对象列表", requiredMode = Schema.RequiredMode.REQUIRED)
    ContractDTO contractDTO;
    /**
     * 可变参数
     */
    @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
    Map<String, Object> resultMap;

}
