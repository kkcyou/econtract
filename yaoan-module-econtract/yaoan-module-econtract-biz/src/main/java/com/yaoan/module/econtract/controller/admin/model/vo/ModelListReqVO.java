package com.yaoan.module.econtract.controller.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.List;


/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 16:36
 */
@Data
@Schema(description = "模板")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ModelListReqVO extends PageParam {

    @Schema(description = "模板名称，模糊匹配", example = "金融服务模板")
    private String name;

    /**
     * 编码
     */
    @Schema(description = "模板编码", example = "demo")
    private String code;

    /**
     * 模板分类ID
     */
    @Schema(description = "模板分类ID，模糊匹配", example = "1")
    private Integer categoryId;

    /**
     * 模板分类ID树状图搜索用
     */
    private List<Integer> categoryIdList;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型ID")
    private String contractType;

    /**
     * 查询创建时间范围的起始时间
     */
    @Schema(description = "查询创建时间范围的起始时间", example = "2023-08-01 01:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startCreateTime;

    /**
     * 查询创建时间范围的结束时间
     */
    @Schema(description = "查询创建时间范围的结束时间", example = "2023-08-01 03:00:00")
    @JsonFormat(timezone="GMT+8")
    private Date endCreateTime;

    /**
     * 查询审批时间范围的开始时间
     */
    @Schema(description = "查询审批时间范围的开始时间", example = "2023-08-01 01:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone="GMT+8")
    private Date startApproveTime;

    /**
     * 查询审批时间范围的结束时间
     */
    @Schema(description = "查询审批时间范围的结束时间", example = "2023-08-02 01:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone="GMT+8")
    private Date endApproveTime;

    /**
     * 查询发布时间范围的开始时间
     */
    @Schema(description = "查询发布时间范围的开始时间", example = "2023-08-01 01:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone="GMT+8")
    private Date startPublishTime;

    /**
     * 查询发布时间范围的结束时间
     */
    @Schema(description = "查询发布时间范围的结束时间", example = "2023-08-02 01:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone="GMT+8")
    private Date endPublishTime;

    /**
     * 创建者/模板名称
     */
    @Schema(description = "创建者/模板名称", example = "a")
    private String searchText;

    /**
     * 时效模式
     */
    @Schema(description = "时效模式", example = "0")
    private Integer timeEffectModel;

    /**
     * 审批状态
     */
    @Schema(description = "审批状态", example = "1")
    private Integer approveStatus;

    /**
     * 模板类型
     */
    private String type;

    /**
     * 生效时间内是否启用 0未启用 1启用
     */
    private Integer effectStatus;

    /**
     * 是否失效 0失效 1有效
     */
    private Integer effective;
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
     * 草拟合同
     */
    private Integer isDraft;
    /**
     * 区划编号
     */
    private String regionCode;
    /**
     * 区划名称
     */
    private String regionName;

    /**
     * 是否查出政采类型模板，0否，1是，不传默认查询带政采类型的模板
     */
    private Integer isGov;
}
