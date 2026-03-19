package com.yaoan.module.econtract.controller.admin.term.vo.tree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/9 16:14
 */
@Data
public class TermTreeRespVO {

    @Schema(description = "条款分类id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "条款分类id不能为空")
    private Integer id;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分类名称不能为空")
    private String name;

    @Schema(description = "父分类id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "父分类id不能为空")
    private Integer parentId;

    @Schema(title = "分类里所对应的条款信息")
    private List<TermTreeDetailRespVO> detailRespVOList;

}
