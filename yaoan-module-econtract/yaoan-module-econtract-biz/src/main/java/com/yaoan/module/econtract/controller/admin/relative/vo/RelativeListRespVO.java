package com.yaoan.module.econtract.controller.admin.relative.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class RelativeListRespVO extends BaseRelativeVO{
    @Schema(description = "相对方id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String categoryName;

    @Schema(description = "创建人姓名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String creatorName;

    @Schema(description = "相对方性质  企业2  个人：1  单位3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String entityTypeName;

    private String creator;

    private String sourceTypeName;
    private String relativeTypeName;
    private String cardTypeName;
    private String statusName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    /**
     * 流程实例id
     * */
    private String processInstanceId;

    public String taskId;

    /**
     * 流程状态
     * */
    private Integer result;
    /**
     * 流程状态
     * */
    private String resultStr;
    /**
     * 审批状态
     */
    private Integer flowStatus;

    /**
     * 提交时间
     * */
    private LocalDateTime submitTime;

    /**
     * 审批时间
     * */
    private LocalDateTime approveTime;

    /** 节点名称 */
    private String nodeName;
}
