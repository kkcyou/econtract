package com.yaoan.module.econtract.api.contract.dto.contract;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class SignatoryRelDTO  {

    private static final long serialVersionUID = 5108606370363217603L;

    /**
     * 主键
     */
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 1. 甲方 2. 乙方 3. 丙方 4. 丁方
     * （旧数据为空，默认就是乙方）
     */
    @Schema(description = "签署方主体角色", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer type;

    /**
     * 签署方id
     */
    private String signatoryId;

    /**
     * 相对方是否签署
     */
    private Integer isSign;
    /**
     * 是否确认 0待处理。1已处理 -1已拒绝
     */
    private Integer isConfirm;

    /**
     * 签署顺序
     */
    private Integer sort;

    /**
     * 相对方负责该合同的联系人id
     */
    private Long contactId;
}