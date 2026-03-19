package com.yaoan.module.econtract.controller.admin.watermark.vo.page;

import com.yaoan.module.econtract.controller.admin.watermark.vo.WatermarkRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2025-9-8 11:34
 */
@Data
public class WatermarkPageRespVO extends WatermarkRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3430")
    private String id;
    @Schema(description = "水印类型 自定义文字水印 = 0 业务字段 = 1", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private String typeName;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
