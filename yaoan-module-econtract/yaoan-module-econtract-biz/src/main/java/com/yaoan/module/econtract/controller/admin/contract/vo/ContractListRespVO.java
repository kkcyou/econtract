package com.yaoan.module.econtract.controller.admin.contract.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Data
public class ContractListRespVO {
    /**
     * 合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractType;
    /**
     * 合同类型
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractTypeName;
    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    /**
     * 任务名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 签署方，我方名称
     */
    @Schema(description = " 签署方，我方名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mySignatory;

    /**
     * 我方签署人姓名
     */
    @Schema(description = " 我方签署人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String signName;
    /**
     * 签署方集合
     */
    @Schema(description = "签署方集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SignatoryRespVO> signatoryList;
    /**
     * 上传合同
     * 默认 0 - 否  通过草拟方式，需要走签章
     * 1 - 是  直接上传合同，可能走履约
     */
    @Schema(description = "合同来源标识", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer upload;
}
