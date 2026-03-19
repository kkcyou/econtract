package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo;

import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/21 19:21
 */
@Data
public class AcceptancePageReqVO extends PageParam {

    /**
     * 0=待发起验收
     * 1=已发起验收
     * 5=已关闭验收
     */
    private Integer isAcceptance;

    private String contractName;
    private String contractCode;

    /**
     * 签署方名字
     */
    private String signatureName;

}
