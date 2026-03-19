package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "获取纠错信息")
@Data
public class ErrorInfosReqVO {

    private String token;

    private String fileId;
}
