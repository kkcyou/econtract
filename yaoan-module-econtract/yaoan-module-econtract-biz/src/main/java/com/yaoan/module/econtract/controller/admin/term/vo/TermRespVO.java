package com.yaoan.module.econtract.controller.admin.term.vo;

import com.yaoan.module.econtract.controller.admin.term.vo.termlabel.TermLabelVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.TermParamVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author doujiale
 */
@Schema(description = "term信息 Response VO")
@Data
public class TermRespVO {


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
    /**
     * 条款分类-左边树形结构
     */
    @Schema(description = "条款分类id")
    private Integer categoryId;
    /**
     * 条款分类名称
     */
    @Schema(description = "条款分类名称")
    private String categoryName;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型")
    private String contractType;
    private List<String> contractTypes;

    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称")
    private String contractTypeName;
    private List<String> contractTypeNames;

    /**
     * 条款描述
     */
    @Schema(description = "条款描述")
    private String remark;

    @Schema(description = "标签列表")
    private List<TermLabelVO> labels;

    @Schema(description = "参数列表")
    private List<TermParamVO> params;

    /**
     * 审批状态
     * {@link com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum}
     */
    private Integer result;
    private String resultName;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 原因说明(审批流)
     */
    private String reason;

    /**
     * 任务id(审批流)
     */
    private String taskId;
    /**
     * 被分派到任务的人
     * */
    private Long assigneeId;

    /**
     * 分类
     */
    private String termKind;

    /**
     * 分类
     */
    private String termKindName;

    /**
     * 所属行业
     */
    private String tradeType;
    private String tradeTypeName;
    /**
     * 条款依据
     */
    private String termAccord;
    /**
     * 条款库类别 0公共库，1单位库，2其他
     */
    private Integer termLibrary;
}
