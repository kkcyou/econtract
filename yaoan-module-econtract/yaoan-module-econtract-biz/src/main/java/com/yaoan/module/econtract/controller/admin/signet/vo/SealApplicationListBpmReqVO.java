package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
public class SealApplicationListBpmReqVO extends PageParam {
    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractName;
    private String contractCode;

    /**
     * 印章名称
     */
    @Schema(description = "印章名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealName;

    /**
     * 审批状态
     */
    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer result;

    /**
     * 申请人id
     */
    @Schema(description = "申请人id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String applicantId;
    /**
     * 申请人名字
     */
    @Schema(description = "申请人名字", requiredMode = Schema.RequiredMode.REQUIRED)
    private String applicantName;


    /**
     * {@link ApprovePageFlagEnums}
     * ALL(0, "全部"),
     * DONE(1, "已审批"),
     * TO_DO(2, "未审批"),
     */
    private Integer flag;

    /**
     * 流程实例
     */
    List<String> instanceIdList;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 搜索任务状态的字段
     */
    private Integer taskResult;
    /**
     * 是否政采
     */
    private Integer isGov;
}
