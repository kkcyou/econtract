package com.yaoan.module.econtract.api.contract.dto;

import com.yaoan.module.econtract.api.contract.dto.gpx.StagePaymentDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025/1/9 18:44
 */
@Data
public class BaseDTO {

    /**
     * 创建者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String creator;

    /**
     * 更新者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String updater;

    /**
     * 阶段支付信息
     */
    @Schema(description = "阶段支付信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<StagePaymentDTO> payMentInfo;
}
