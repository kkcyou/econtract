package com.yaoan.module.econtract.controller.admin.order.vo;

import cn.hutool.core.date.DateTime;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.enums.order.SortEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GPMallPageReqVO extends PageParam {
    List<String> orderIdList;
    /**
     * 订单编码
     */
    private String orderCode;
    /**
     * 供应商
     */
    private String supplierName;
    /**
     * 采购人
     */
    private String purchaserOrgName;
    /**
     * 下单时间start
     */
    private DateTime payTime0;
    /**
     * 下单时间end
     */
    private DateTime payTime1;
    /**
     * 订单总额start
     */
    private BigDecimal orderTotalAmount0;
    /**
     * 订单总额end
     */
    private BigDecimal orderTotalAmount1;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 订单分类
     */
    private String orderCategory;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 下单时间排序规则
     * {@link SortEnums}
     */
    private String payTimeSort;
    /**
     * 订单总额排序规则
     * {@link SortEnums}
     */
    private String totalAmountSort;
    /**
     * 计划编号
     */
    private String buyPlanCode;
    /**
     * 计划编号
     */
    private String buyPlanName;
    /**
     * 合同来源（电子卖场：gpmall-5.3，电子交易（项目采购）：gpms-gpx-5.3，框采平台：gp-gpfa）
     */
    @NotBlank(message = "合同来源不能为空")
    @Schema(description = "合同来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractFrom;
    /**
     * 查询类型：0：待起草  1：已起草  2：已取消
     */
    @NotBlank(message = "查询类型不能为空")
    @Schema(description = "查询类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer flag;
    /**
     * 平台码
     */
    private String platformCode;
    /**
     * 采购单位id
     */
    private String orgId;

}
