package com.yaoan.module.econtract.controller.admin.relation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 合同关联关系列表返回
 *
 * @author doujl
 * @since 2023-07-24
 */
@Data
@Schema(description = "返回关联关系列表")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class IncidenceRelationResplistVO extends BaseIncidenceRelationVO {
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private  String contractName;
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private  String contractCode;

    /**
     * 关联关系id
     */
    @Schema(description = "关联关系id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractId;
    /**
     * 关联合同id
     */
    @Schema(description = "关联合同id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String relationContractId;
    /**
     * 文件ID
     */
    @Schema(description = "文件ID ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long fileId;
    /**
     * 文件名称
     */
    @Schema(description = "文件名称 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;
    /**
     * 文件存储路径
     */
    @Schema(description = "文件存储路径 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileurl;


}
