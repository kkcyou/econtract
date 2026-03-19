package com.yaoan.module.econtract.controller.admin.ledger.vo;

import cn.hutool.core.date.DateTime;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author doujiale
 */
@Schema(description = "LedgerPageReqV2VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class LedgerPageReqV2VO extends PageParam {


    private static final long serialVersionUID = 6459845177432424840L;
    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String code;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     * 签署截止日期-开始时间
     */
    @Schema(description = "合同生效日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime beginDate;

    /**
     * 签署截止日期-结束时间
     */
    @Schema(description = "合同有效期结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime endDate;

    /**
     * 签署截止日期-开始时间
     */
    @Schema(description = "签署截止日期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime expirationDate0;

    /**
     * 签署截止日期-结束时间
     */
    @Schema(description = "签署截止日期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime expirationDate1;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> contractType;

    /**
     * 签署状态
     * 0-待发送 - 新增
     * 1-被退回
     * 2-已发送
     * 3-待确认
     * 4-待签署
     * 5-已拒签
     * 6-签署完成
     * 7-逾期未签署
     */
    @Schema(description = "签署状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<Integer> status;

    /**
     * 0、付款
     * 1、收款
     * 2、不结算
     * 3、收支双向
     */
    @Schema(description = "合同结算方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<Integer> amountType;

    @Schema(description = "甲方名称")
    private String partAName;
    @Schema(description = "乙方名称")
    private String partBName;

    @Schema(description = "最大合同金额")
    private Double maxAmount;
    @Schema(description = "最小合同金额")
    private Double minAmount;

    private List<String> processInstanceIds;
    /**
     * 签署日期0
     * */
    private DateTime signDate0;

    /**
     * 签署日期1
     * */
    private DateTime signDate1;
}