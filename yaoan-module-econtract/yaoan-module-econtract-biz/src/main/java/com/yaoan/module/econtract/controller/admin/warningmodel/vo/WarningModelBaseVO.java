package com.yaoan.module.econtract.controller.admin.warningmodel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 预警模块来源（new预警） Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class WarningModelBaseVO {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称", example = "张三")
    private String name;

    @Schema(description = "父级节点id", example = "19918")
    private String parentId;

}
