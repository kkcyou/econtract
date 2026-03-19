package com.yaoan.module.econtract.controller.admin.change.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.AttachmentVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangePaymentDO;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description: 保存补充协议 ReqVO
 * @author: Pele
 * @date: 2024/1/24 16:47
 */
@Data
public class ContractChangeStatusSaveReqVO {
    /**
     * 变动id
     */
    @Schema(description = "变动id")
    private String id;

    /**
     * 合同主键
     */
    @Schema(description = "合同主键")
    @NotNull(message = "合同id不能为空")
    private String mainContractId;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码")
    @NotNull(message = "合同编码123不能为空")
    private String contractCode;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称")
    @NotNull(message = "合同名称不能为空123")
    private String contractName;

    /**
     * 合同金额
     */
    @Schema(description = "合同金额")
    private String amount;

    /**
     * 变动合同名称
     */
    @Schema(description = "变动合同名称")
    @NotNull(message = "变动合同名称不能为空")
    private String changeName;

    /**
     * 变动合同编号
     */
    @Schema(description = "变动合同编号")
    private String changeCode;

    /**
     * 变动类型（1=变更，2=补充，3=解除）
     * {@link com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums}
     */
    @NotNull(message = "变动类型不能为空")
    @Schema(description = "变动类型")
    private Integer changeType;

    /**
     * 申请原因
     */
    @Schema(description = "申请原因")
    private String reason;

    /**
     * 主文件地址id
     */
    @Schema(description = "主文件地址id")
    private Long fileAddId;

    /**
     * 提交标识
     */
    private Integer isSubmit;

    /**
     * 审批意见
     */
    private String advice;
    /**
     * 附件文件
     */
    @Schema(description = "附件文件")
    private List<AttachmentVO> files;
}
