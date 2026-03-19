package com.yaoan.module.econtract.controller.admin.outward.contract.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.contract.vo.AttachmentRelCreateReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractParameterDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ApiCreateReqVO implements Serializable {
    private static final long serialVersionUID = 0xb77bb1a8523c0a64L;

    /**
     * id-主键
     */
    @Schema(description = "id-主键", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractName;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractCode;

    /**
     * 合同文件内容
     */
    @Schema(description = "合同文件")
    private String contractContent;
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
     * 起草业务数据
     */
    @Schema(description = "草拟业务数据", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Object contractData;

    /**
     * 附件集合
     */
    @Schema(description = "附件集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<AttachmentRelCreateReqVO> attachmentList;

    /**
     * 服务器异步通知路径
     */
    @Schema(description = "服务器异步通知路径", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String notifyUrl;

    /**
     * 参数集合
     */
    @Schema(description = "参数集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ContractParameterDO> contractParameters;
}
