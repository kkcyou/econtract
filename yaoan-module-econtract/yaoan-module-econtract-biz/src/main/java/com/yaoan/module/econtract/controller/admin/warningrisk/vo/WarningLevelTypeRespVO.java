package com.yaoan.module.econtract.controller.admin.warningrisk.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 风险规则返回VO，该VO包含不同规则的所有字段
 */

@Schema(description = "预警管理后台 -  风险等级VO")
@Data
public class WarningLevelTypeRespVO {
    @Schema(description = "id")
    private String id;

    @Schema(description = "预警级别")
    private Integer level;

    @Schema(description = "预警级别名称")
    private String name;

    @Schema(description = "预警颜色")
    private String levelColor;

}
