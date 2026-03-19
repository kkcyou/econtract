package com.yaoan.module.econtract.controller.admin.ledger.vo;

import com.yaoan.module.econtract.controller.admin.contract.vo.SignatoryRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/19 19:32
 */
@Data
public class LedgerRelationContractRespVO {



    /**
     * 合同id
     */
    @Schema(description = "合同id ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractId;
    /**
     * 合同名称
     */
    @Schema(description = "合同名称 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractName;
    /**
     * 关联合同id
     */
    @Schema(description = "关联合同id ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String relationContractId;
    /**
     * 关联关系
     */
    @Schema(description = "关联关系 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer incidenceRelation;

    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractTypeName;
    /**
     * 关联时间
     */
    @Schema(description = "关联时间 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime createTime;

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
    /**
     * 签署方，我方名称
     */
    private String mySignatory;

    /**
     * 我方签署人姓名
     */
    private String signName;
    /**
     * 签署方集合
     */
    @Schema(description = "签署方集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<SignatoryRespVO> signatoryList;







}
