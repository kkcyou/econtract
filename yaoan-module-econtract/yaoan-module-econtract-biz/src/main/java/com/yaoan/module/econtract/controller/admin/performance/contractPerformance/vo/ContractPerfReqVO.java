package com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
@Data
public class ContractPerfReqVO  {

    /**
     * 合同id
     */
    private String contractId;
    /**
     * 合同编码
     */
    private String contractCode;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 签署完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date signFinishTime;
    /**
     * 合同类型id
     */
    private String contractTypeId;

    /**
     * 合同状态值
     */
    private Integer contractStatus;
    /**
     * 合同有效期-开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date validity0;

    /**
     * 合同有效期-结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date validity1;

}
