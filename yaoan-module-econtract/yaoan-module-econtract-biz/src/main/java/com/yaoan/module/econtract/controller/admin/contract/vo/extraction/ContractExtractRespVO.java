package com.yaoan.module.econtract.controller.admin.contract.vo.extraction;

import com.yaoan.module.econtract.controller.admin.contract.vo.AcceptRequirement;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractSignatoryReqVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativeByUserRespVO;
import com.yaoan.module.econtract.controller.admin.supervise.vo.ContractPayeeInfoVO;
import com.yaoan.module.econtract.controller.admin.supervise.vo.PaymentPlanVO;
import com.yaoan.module.econtract.controller.admin.supervise.vo.SuperviseGoodsVO;
import com.yaoan.module.econtract.controller.admin.supervise.vo.SuperviseProjectVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
public class ContractExtractRespVO {

  /**
   * 合同基本信息
   */
  @Schema(description = "合同基本信息")
  private ContractBaseInfoRespVo baseInfo;
  /**
   * 签订方信息
   */
  @Schema(description = "签订方信息")
  private List<RelativeByUserRespVO> signatoryInfoList;

  /**
   * 收款账户信息
   */
  @Schema(description = "收款账户信息")
  private ContractPayeeInfoVO contractPayeeInfoVO;

  /**
   * 采购标的信息
   */
  @Schema(description = "采购标的信息")
  private List<SuperviseGoodsVO> goodsList;

  /**
   * 支付计划信息
   */
  @Schema(description = "支付计划信息")
  private List<PaymentPlanVO> paymentPlanList;

  /**
   * 项目基本信息
   */
  @Schema(description = "项目基本信息")
  private SuperviseProjectVO projectInfo;

  /**
   * 履约验收要求
   */
  @Schema(description = "履约验收要求")
  private AcceptRequirement acceptRequirement;
}
