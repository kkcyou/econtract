package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Schema(description = "Contract PageRequest VO")
@Data
public class ContractRepVO {
    private static final long serialVersionUID = -4802787786314022100L;
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    private  String id;
    @Schema(description = "搜索字符串，用于模糊匹配合同编码，合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String searchText;
}
