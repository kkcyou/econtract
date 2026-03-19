package com.yaoan.module.econtract.controller.admin.contract.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import com.yaoan.module.econtract.service.gcy.gpmall.vo.ContractPaymentPlanVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Schema(description = "上传已签署的合同请求参数VO")
@Data
@ToString(callSuper = true)
public class UploadSignedContractVO {
    private static final long serialVersionUID = -1532352919770058260L;

    /**
     * id-主键
     */
    @Schema(description = "id-主键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private String id;

    /**
     * 签署文件名称
     */
    @Schema(description = "签署文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;

    /**
     * 签署文件地址id
     */
    @Schema(description = "签署文件地址id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fileAddId;

    /**
     * 合同文件pdf 地址id
     */
    @Schema(description = "合同文件pdf地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pdfFileId;
    
}
