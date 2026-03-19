package com.yaoan.module.econtract.controller.admin.order.vo;

import com.yaoan.module.econtract.api.gcy.order.GoodsVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GPMallPageRespVO {
    /**
     * 订单主键id
     */
    private String id;

    /**
     * 订单Guid
     */
    private String orderGuid;

    /**
     * 订单编码
     */
    private String orderCode;
    /**
     * 订单类型
     *
     */
    private String orderType;
    /**
     * 订单类型
     * {@link }
     */
    private String orderTypeStr;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单状态名称
     */
    private String orderStatusStr;
    /**
     * 订单总额
     */
    private BigDecimal orderTotalAmount;
    /**
     * 采购单位id
     */
    private String purchaserOrgGuid;
    /**
     * 采购单位
     */
    private String purchaserOrg;
    /**
     * 区划名称
     */
    private String regionFullName;

    /**
     * 区划id
     */
    private String regionGuid;

    /**
     * 下单时间
     */
    private Date payTime;
    private String payTimeBack;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商id
     */
    private String supplierGuid;


    /**
     * 模板Id
     */
    private String modelId;

    /**
     * 计划编号
     */
    private String buyPlanCode;

    /**
     * 计划名称
     */
    private String buyPlanName;

    /**
     * 计划金额
     */
    private BigDecimal buyPlanAmount;
    /**
     * 合同来源（电子卖场：gpmall-5.3，电子交易（项目采购）：gpms-gpx-5.3，框采平台：gp-gpfa）
     */
    private String contractFrom;
    /**
     * 订单状态（0=待起草，1=已起草合同）

     */
    private String status;
    /**
     * 交易方式

     */
    @Schema(description = "交易方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String tradingType;

    /**
     * 交易方式名称 (使用卖场订单来源枚举)

     */
    @Schema(description = "交易方式名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String tradingTypeName;
    /**
     * 是否需要起草 0否 1是----数据库需要设置默认值为1
     */
    @Schema(description = "是否需要起草", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isDraft;

    /**
     * 商品（上传文件用）
     */
    private List<GoodsRespVO> goodsRespVOList;

    /**
     * 标的信息（电子合同用）
     */
    @Schema(description = "标的信息", requiredMode = Schema.RequiredMode.REQUIRED)
    List<GoodsVO> goodsVOS;

    /** 采购人地址 */
    private String buyerAddress;

    /** 采购人名称 */
    private String buyerName;

    /** 采购人电话 */
    private String buyerTel;
    /**
     * 开户银行账号
     */
    private String bankName;

    /**
     * 开户银行
     */
    private String bankAccount;

    /**
     * 供应商联系人
     */
    private String supplierLinkman;

    /**
     * 供应商地址
     */
    private String supplierAddr;

    /**
     * 供应商传真
     */
    @Schema(description = "供应商传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierlinkFax;

    /**
     * 供应商电话
     */
    private String supplierTel;

    /**
     * 企业规模（大、中、小、微、其他）(提示：填写''营业收入''和''从业人员''信息后，系统会自动做出企业规模认定。)0 大型企业\n4 微型企业\n1 中型企业\n2
     * 小型企业\n3 其他
     */
    private String unitScopeCode;

    private String nowStatusStr;

    /**
     * 项目所属分类（A、B、C）
     */
    private String projectCategoryCode;
}
