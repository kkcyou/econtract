package com.yaoan.module.econtract.controller.admin.bpm.register.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/26 15:02
 */
@Data
public class ContractRegisterListApproveReqVO extends CommonBpmAutoPageReqVO {

    /**
     * 签署日期0
     */
    private Date signDate0;

    /**
     * 签署日期1
     */
    private Date signDate1;

    /**
     * 签署类型
     * 0 - 电子签署
     * 1 - 纸质签署
     */
    private Integer signType;

    /**
     * 合同金额0
     */
    private Double amount0;

    /**
     * 合同金额1
     */
    private Double amount1;
}
