package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Schema(description = "合同批量转交数据请求参数 VO")
@Data
public class TransferContractAllReqVO extends PageParam {


    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     * 全部，
     * 1:进行中(4：待签署:5：已拒签，2：待确认（采购人已发送）+IsSign=0)，
     * 2：已完成（6已完成+IsSign=1），
     * 3：已过期（7：逾期未签署+逾期已签署：签署时间》最后有效时间）
     */
    @Schema(description = "合同状态")
    private Integer statusFlag;


}

