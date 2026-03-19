package com.yaoan.module.econtract.controller.admin.gpx.vo.autofill;

import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.PurchaseVO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/6/2 22:34
 */
@Data
public class AutoFillRespVO {
    private PackageRespVO packageRespVO;
    private List<PurchaseVO> packageDetailRespVOList;
    private PartARespVO partARespVO;
    private ContractBaseRespVO contractBaseRespVO;
}
