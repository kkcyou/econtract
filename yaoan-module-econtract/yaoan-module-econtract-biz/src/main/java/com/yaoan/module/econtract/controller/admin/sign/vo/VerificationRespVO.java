package com.yaoan.module.econtract.controller.admin.sign.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author doujiale
 */
@Schema(description = "合同文件 Request VO")
@Data
@Builder
public class VerificationRespVO {

    @Schema(description = "签章个数")
    private Integer signNum;
    @Schema(description = "签章信息")
    private List<SignInfoVO> signInfo;


}
