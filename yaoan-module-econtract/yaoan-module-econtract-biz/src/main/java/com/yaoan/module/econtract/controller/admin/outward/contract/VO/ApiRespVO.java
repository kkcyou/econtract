package com.yaoan.module.econtract.controller.admin.outward.contract.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.contract.vo.AttachmentRelRespVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractParameterDO;
import com.yaoan.module.econtract.dal.dataobject.paramModel.ParamModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@ToString(callSuper = true)
public class ApiRespVO implements Serializable {
    private static final long serialVersionUID = 4647397149382580379L;

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 合同文件内容
     */
    @Schema(description = "合同文件内容", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractContent;

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
     * 8-合同终止签署中
     * 9-合同终止
     * 10-合同变更
     * 11-待送审
     * 12-审核中
     * 13审核未通过
     */
    @Schema(description = "签署状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;

    /**
     * 合同文件地址
     */
    @Schema(description = "合同文件地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractFileUrl;

    /**
     *合同金额
     */
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double amount;

    /**
     *合同有效期-开始时间
     */
    @Schema(description = "合同有效期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date effectDate;

    /**
     *合同有效期-结束时间
     */
    @Schema(description = "合同有效期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date expiryDate;

    /**
     *关联的模板id
     */
    @Schema(description = "关联的模板id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String templateId;

    /**
     * 附件集合
     */
    @Schema(description = "附件集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<AttachmentRelRespVO> attachmentList;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
