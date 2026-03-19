package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
@Schema(description = "电子合同 - 通过流程任务的 Request VO")
@Data
public class BpmReqVO {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractCode;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractName;

    /**
     *签署截止日期
     */
    @Schema(description = "签署截止日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date expirationDate;

    /**
     *合同分类
     */
    @Schema(description = "合同分类", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer contractClassification;

    /**
     *合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractType;

    /**
     *合同描述
     */
    @Schema(description = "合同描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractDescription;

    /**
     *签署文件名称
     */
    @Schema(description = "签署文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;

    /**
     *主文件地址
     */
    @Schema(description = "主文件地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileAdd;

    /**
     *附件id
     */
    @Schema(description = "附件id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String attachmentId;

    /**
     * 签署状态
     * 0-待发送 - 新增
     * 1-被退回
     * 2-已发送
     * 3-待确认
     * 4-待签署
     * 5-已拒签
     * 6-签署完成
     * 7-逾期未签署
     */
    @Schema(description = "签署状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;

    /**
     *任务编号
     */
    @Schema(description = "任务编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String taskId;
}
