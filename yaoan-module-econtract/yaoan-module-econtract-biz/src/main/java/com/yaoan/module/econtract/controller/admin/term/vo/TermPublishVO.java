package com.yaoan.module.econtract.controller.admin.term.vo;

import com.yaoan.module.econtract.controller.admin.term.vo.termlabel.LabelVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.ParamVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Description;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 条款新增或修改Vo
 *
 * @author huanghongwei
 * @date 2021/1/5
 */
@Data
@Description("条款修改Vo")
public class TermPublishVO {

    /**
     * 条款ID
     */
    @Schema(description = "条款ID")
    private String id;

    /**
     * 条款编码
     */
    @NotBlank(message = "条款编码不可为空")
    @Schema(description = "条款编码")
    private String code;

    /**
     * 条款名称
     */
    @NotBlank(message = "条款名称不可为空")
    @Schema(description = "条款名称")
    private String name;

    /**
     * 条款类型(head合同封页，com合同条款，end合同结尾)
     */
    @NotBlank(message = "条款类型不可为空")
    @Schema(description = "条款类型(head合同封页，com合同条款，end合同结尾)")
    private String termType;

    /**
     * 条款内容
     */
    @NotBlank(message = "条款内容不可为空")
    @Schema(description = "条款内容")
    private String termContent;


    /**
     * 条款描述
     */
    @Schema(description = "条款描述")
    private String remark;

    /**
     * 条款分类-左边树形结构
     */
    @Schema(description = "条款分类id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "条款分类id不能为空")
    private Integer categoryId;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "合同类型不能为空")
    private String contractType;

    /**
     * 标签列表
     */

    @Schema(description = "标签列表")
    private List<LabelVO> labels;

    /**
     * 参数列表
     */

    @Schema(description = "参数列表")
    private List<ParamVO> params;
}
