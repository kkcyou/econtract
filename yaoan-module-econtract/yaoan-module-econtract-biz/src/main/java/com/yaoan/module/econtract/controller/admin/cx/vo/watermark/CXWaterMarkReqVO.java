package com.yaoan.module.econtract.controller.admin.cx.vo.watermark;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description: 畅写水印
 * @author: Pele
 * @date: 2024/4/25 19:08
 */
@Data
public class CXWaterMarkReqVO {

    /**
     * 文档路径，必填
     */
    @NotNull(message = "文档路径，必填")
    private String fileUrl;

    /**
     * 水印内容，必填
     */
    @NotNull(message = "水印内容，必填")
    private String waterText;

    /**
     * 是否倾斜，非必填，默认为水平显示
     */
    private Boolean bIsDiagonal;


}
