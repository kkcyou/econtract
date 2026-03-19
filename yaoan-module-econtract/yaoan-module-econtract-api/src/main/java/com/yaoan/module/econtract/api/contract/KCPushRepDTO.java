package com.yaoan.module.econtract.api.contract;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 合同信息对象
 *
 * @author zhc
 * @since 2023-12-05
 */
@Data
public class KCPushRepDTO {
    /**
     * 订单id
     */
    @NotBlank(message = "订单id不可传空")
    private String orderGuid;

    /**
     * 合同ID
     */
    @NotBlank(message = "合同id不可传空")
    private String contractGuid;

    /**
     * "合同状态(-1:已取消,-2：已删除 0:草稿,1:待确认,2:已确认,3:供应商盖章,4:完成)")
     */
    @NotNull(message = "合同状态不可传空")
    private Integer contractStatus;
    /**
     * "备案状态(0=未备案(没推到监管) 1=已备案(监管已备案) 3=备案中(推过去没备案)
     */
    @NotNull(message = "合同状态不可传空")
    private Integer isBak;


}
