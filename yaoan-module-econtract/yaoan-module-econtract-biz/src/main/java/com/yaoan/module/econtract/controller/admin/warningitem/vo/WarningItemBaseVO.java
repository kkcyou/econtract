package com.yaoan.module.econtract.controller.admin.warningitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 预警事项表（new预警） Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class WarningItemBaseVO {

    @Schema(description = "检查点id", example = "29497")
    private String configId;

    @Schema(description = "预警事项名称", example = "芋艿")
    private String itemName;

    @Schema(description = "风险说明", example = "你猜")
    private String itemRemark;

}
