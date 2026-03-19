package com.yaoan.module.econtract.controller.admin.warning.vo;
import lombok.Data;

@Data
public class NotSignedContractInfoRespVO {
    /**
     * 合同信息
     */
    private ContractInfoVO contractInfo;

    /**
     * 预警信息
     */
    private WarningInfoVO warningInfo;

    /**
     * 信息
     */
    private PackageInfoVO packageInfo;
}
