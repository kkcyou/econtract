package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.dal.dataobject.category.TemplateCategory;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/23 8:15
 */
@Data
@Schema(description = "范本")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TemplateListReqVo extends PageParam {

    private static final long serialVersionUID = 7779560071605445862L;
    /**
     * 范本主键ID
     */
    @Schema(description = "范本主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String id;

    /**
     * 范本名称
     */
    @Schema(description = "范本名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String name;

    /**
     * 范本编号
     */
    @Schema(description = "范本编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String code;

    /**
     * 范本分类
     */
    @Schema(description = "范本分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer templateCategoryId;

    /**
     * 范本分类集合
     */
    @Schema(description = "范本分类集合", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private List<Integer> templateCategoryList;

    /**
     * 范本来源(官方范本 或 标准范本)
     */
    @Schema(description = "范本来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String templateSource;

    /**
     * 发布机构
     */
    @Schema(description = "发布机构", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String publishOrgan;

    /**
     * 发布时间起始（查询范围）
     */
    @Schema(description = "发布时间起始（查询范围）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-01-01 09:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startPublishTime;

    /**
     * 发布时间结束（查询范围）
     */
    @Schema(description = "发布时间结束（查询范围）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-11-01 09:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endPublishTime;


    /**
     * 审批状态（0=未开启审批，1=审批中，2=审批未通过，3=审批通过）
     */
    private Integer approveStatus;

    /**
     * 模糊搜索内容
     */
    @Schema(description = "模糊搜索内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String searchText;

    /**
     * 前端发送的状态码
     * {@link CommonFlowableReqVOResultStatusEnums}
     *     TO_SEND(0, "TO_SEND", "草稿"),
     *     APPROVING(1, "APPROVING", "已审批"),
     *     TO_DO(2, "SUCCESS", "审批通过"),
     *     REJECTED(3, "TO_SEND", "被退回"),
     */
    private String frontCode;


    /**
     * 创建时间结束（查询范围）
     */
    @Schema(description = "发布时间结束（查询范围）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-11-01 09:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime0;

    /**
     * 创建时间结束（查询范围）
     */
    @Schema(description = "发布时间结束（查询范围）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-11-01 09:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime1;

    /**
     * 草拟模板
     */
    @Schema(description = "草拟模板", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private Integer isDraft;

    /**
     * 发布情况 0-未发布 1-已发布
     */
    private Integer publishStatus;

    private List<Integer> categoryIdList;
    private  List<TemplateCategory> allCategories;
    //是否政府采购 0否1是
    private Integer isGov;
    private Integer uploadType;
}
