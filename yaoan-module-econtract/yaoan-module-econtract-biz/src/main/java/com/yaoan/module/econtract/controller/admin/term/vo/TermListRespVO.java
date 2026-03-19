package com.yaoan.module.econtract.controller.admin.term.vo;

import com.yaoan.module.econtract.controller.admin.term.vo.termlabel.TermLabelVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.TermParamRespVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.TermParamVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author doujiale
 */
@Schema(description = "根据条款制作模板时展示列表 VO")
@Data
public class TermListRespVO {
    @Schema(description = "条款id")
    private String id;

    @Schema(description = "条款编码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "条款类型")
    private String termType;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "条款内容")
    private String termContent;

    @Schema(description = "条款分类id")
    private Integer categoryId;


    @Schema(description = "合同类型")
    private String contractType;

    @Schema(description = "参数列表")
    private List<TermParamRespVO> params;

}
