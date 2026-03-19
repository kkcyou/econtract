package com.yaoan.module.econtract.controller.admin.term.vo.termparam;

import com.yaoan.module.econtract.controller.admin.param.vo.BaseParamVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Description;

/**
 * 查询条款时关联参数vo
 *
 * @author doujl
 */
@Data
@Description("条款关联参数实体")
public class TermParamRespVO extends BaseParamVO {
    /**
     * 合同条款id
     */
//    private String termId;

    @Schema(description = "主键")
    private String id;

    @Schema(description = "合同参数编号")
    private Integer paramNum;
    /**
     * 条款分类-左边树形结构
     */
    @Schema(description = "条款分类id")
    private Integer categoryId;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型")
    private String contractType;

}
