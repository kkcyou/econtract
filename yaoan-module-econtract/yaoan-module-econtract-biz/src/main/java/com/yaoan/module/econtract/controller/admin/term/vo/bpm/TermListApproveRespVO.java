package com.yaoan.module.econtract.controller.admin.term.vo.bpm;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableParam;
import com.yaoan.module.econtract.enums.term.TermTypeEnums;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/10 16:23
 */
@Data
public class TermListApproveRespVO extends FlowableParam {

    /**
     * 主键
     */
    private String id;

    /**
     * 条款编码
     */
    private String code;

    /**
     * 条款名称
     */
    private String name;

    /**
     * 状态 0未发布 1已发布
     */
    private String status;

    /**
     * 条款类型
     */
    private String termType;

    /**
     * 条款分类-左边树形结构
     */
    private Integer categoryId;
    private String categoryName;

    /**
     * 合同类型
     */
    private String contractType;
    private List<String> contractTypes;
    private String contractTypeName;
    private List<String> contractTypeNames;

    /**
     * 条款内容
     */
    private String termContent;

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
     * 申请时间
     */
    private LocalDateTime ApplyTime;

    /**
     * 审批时间
     */
    private LocalDateTime ApproveTime;

    /**
     * 流程任务id
     */
    private String taskId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    private String creator;
    private String creatorName;

    private Integer isOperate;
}
