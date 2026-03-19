package com.yaoan.module.econtract.api.changxie.dto.watermark;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 20:09
 */
@Data
public class CXWaterMarkReqDTO {
    /**
     * 文档路径，必填
     */
    @NotNull(message = "文档路径，必填")
    private String fileUrl;

    /**
     * 水印内容，必填
     */
    private String sText;

    /**
     * 是否倾斜，非必填，默认为水平显示
     */
    private Boolean bIsDiagonal;

}
