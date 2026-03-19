
package com.yaoan.module.econtract.controller.admin.supervise.vo;

import com.yaoan.module.econtract.api.contract.dto.ContractFileDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建合同请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContactCreateReqVO extends ContactBaseVO {
/**
 * 1.合同金额不可大于包的最高限价
 */
    /**
     * 附件对象列表
     */
    @Schema(description = "附件对象列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "附件对象列表不能为空")
    @Valid
    private List<ContractFileDTO> buyReqAttachment;
    /**
     * 合同供应商信息--ContractSupplierMapper
     */
    @Schema(description = "合同供应商信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同供应商信息不能为空")
    @Valid
    private List<SupplierVO> supplierInfo;
    /**
     * 项目信息--ContractProjectMapper
     */
    @Schema(description = "项目信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目信息不能为空")
    @Valid
    private SuperviseProjectVO projectVO;
    /**
     * 支付计划信息--ContractPaymentPlanMapper
     */
    @Schema(description = "支付计划信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "支付计划信息不能为空")
    @Valid
    private List<PaymentPlanVO> paymentPlanList;
    /**
     * 收款账户信息--ContractPayeeAccountMapper
     */
    @Schema(description = "收款账户信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "收款账户信息不能为空")
    @Valid
    private List<ContractPayeeInfoVO> contractPayeeInfoVOS;
    /**
     * 采购标的信息--ContractGoodsMapper
     */
    @Schema(description = "采购标的信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "采购标的信息不能为空")
    @Valid
    private List<SuperviseGoodsVO> goodsList;
}








