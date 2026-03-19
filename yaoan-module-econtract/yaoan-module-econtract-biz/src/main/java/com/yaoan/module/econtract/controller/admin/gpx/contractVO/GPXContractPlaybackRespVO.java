package com.yaoan.module.econtract.controller.admin.gpx.contractVO;

import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.PurchaseVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.SupplierCombinationInfoVO;
import com.yaoan.module.econtract.dal.dataobject.gpx.BidConfirmQuotationDetailDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class GPXContractPlaybackRespVO {


    private String orgName;

    private String orgBankName;
    private String buyerBankAccountName;
    private String orgTaxpayerNum;
    private String orgAddress;
    private String orgBankAccount;
    private String orgFax;

    /**
     * 供应商规模
     */
    private Integer supplierSize;
    /**
     * 供应商特殊性质
     */
    private Integer supplierFeatures;
    
    private String buyPlanBillGuid;

    private String supplierName;
    private String bidCode;
    private String projectCode;
    private String projectName;
    private Double totalMoney;
    private Double amount;
    private String shiftMoney;
    private String deliveryAddress;
    /**
     * 采购方式名称
     */
    private String purchaseMethodName;
    /**
     * 采购方式编码
     */
    private String purchaseMethodCode;

    private String buyerLink;
    private String buyerProxy;
    private String buyerBank;
    private String buyerLinkMobile;
    private String registeredAddress;
    private String supplierLink;
    private String supplierProxy;
    private String supplierFax;
    private String supplierCode;
    private String supplierOrgCode;
    private String supplierLinkMobile;
    private String unitScopeCode;
    private String bankName;
    private String bankAccount;
    private String supplierBankAccountName;
    private String contractName;
    private String planCode;

    private List<PurchaseVO> packageDetailRespVOList;

    private List<BidConfirmQuotationDetailDO> bidConfirmDetailList;
    /**
     * 模板idList
     */
    @Schema(description = "模板idList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> templateIdList;


    /**
     * 联合体信息
     */
    private List<SupplierCombinationInfoVO> supplierCombinationList;

    /**
     * 合同类型 = 项目类型
     */
    private Integer contractType;


}
