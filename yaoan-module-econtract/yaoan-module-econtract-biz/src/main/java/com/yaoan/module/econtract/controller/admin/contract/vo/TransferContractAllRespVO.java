package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Schema(description = "合同批量转交数据返回 VO")
@Data
public class TransferContractAllRespVO {
    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    @Schema(description = "序号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private int sort;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     * c
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "发起时间 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime sendTime;


    /**
     * 发起方
     */
    @Schema(description = "发起方")
    private String initiator;

    /**
     * 签署方集合
     */
    @Schema(description = "签署方集合")
    private List<String> signatoryList;

    @Schema(description = "合同状态")
    private String status;

    /**
     * 合同有效期-结束时间
     */
    @Schema(description = "合同有效期-结束时间")
    private Date validity1;

}

