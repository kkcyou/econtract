package com.yaoan.module.econtract.dal.dataobject.statistics;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_data_statistics")
public class StatisticsDO extends BaseDO {

    private static final long serialVersionUID = -974943648895594166L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 订单id
     */
    @TableField(value = "order_id")
    private String orderId;

    /**
     * 订单编号
     */
    @TableField(value = "order_code")
    private String orderCode;

    /**
     * 订单状态
     */
    @TableField(value = "order_status")
    private Integer orderStatus;

    /**
     * 订单状态名称
     */
    @TableField(value = "order_status_str")
    private String orderStatusStr;

    /**
     * 包id
     */
    @TableField(value = "package_id")
    private String packageId;

    /**
     * 包号
     */
    @TableField(value = "package_number")
    private Integer packageNumber;

    /**
     * 包名称
     */
    @TableField(value = "package_name")
    private String packageName;

    /**
     * 包金额
     */
    @TableField(value = "package_amount")
    private String packageAmount;
    /**
     * 合同id
     */
    @TableField(value = "contract_id")
    private String contractId;
    /**
     * 合同编号
     */
    @TableField(value = "contract_code")
    private String contractCode;

    /**
     * 合同名称
     */
    @TableField(value = "contract_name")
    private String contractName;

    /**
     * 合同状态
     */
    @TableField(value = "contract_status")
    private Integer contractStatus;

    /**
     * 合同金额
     */
    @TableField(value = "contract_money")
    private BigDecimal contractMoney;

    /**
     * 合同签订时间
     */
    @TableField(value = "contract_sign_time")
    private Date contractSignTime;

    /**
     * 采购单位id
     */
    @TableField(value = "org_id")
    private String orgId;

    /**
     * 采购单位名称
     */
    @TableField(value = "org_name")
    private String orgName;

    /**
     * 供应商id
     */
    @TableField(value = "supplier_id")
    private String supplierId;

    /**
     * 供应商名称
     */
    @TableField(value = "supplier_name")
    private String supplierName;

    /**
     * 区划编号
     */
    @TableField(value = "region_code")
    private String regionCode;

    /**
     * 区划名称
     */
    @TableField(value = "region_name")
    private String regionName;

    /**
     * 合同来源
     */
    @TableField(value = "platform")
    private String platform;

    /**
     * 中标金额/订单总额
     */
    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 计划编号
     */
    @TableField(value = "buy_plan_code")
    private String buyPlanCode;

    /**
     * 中标时间
     */
    @TableField(value = "win_bid_time")
    private LocalDateTime winBidTime;

    /**
     * 合同签章时间
     */
    @TableField(value = "sign_time")
    private LocalDateTime signTime;

    /**
     * 签订超期标识 0：签订未超期 1：签订已超期
     */
    @TableField(value = "sign_flag")
    private Integer signFlag;

    /**
     * 合同备案时间
     */
    @TableField(value = "bak_date")
    private LocalDateTime bakDate;

    /**
     * 备案超期标识 0：备案未超期 1：备案已超期
     */
    @TableField(value = "bak_flag")
    private Integer bakFlag;

    /**
     * 采购方式
     */
    @TableField(value = "purchase_method_code")
    private String purchaseMethodCode;

    /**
     * 采购方式名称
     */
    @TableField(value = "purchase_method_name")
    private String purchaseMethodName;

    /**
     * 所属分类 A:货物 B：工程 C：服务
     */
    @TableField(value = "project_category_code")
    private String projectCategoryCode;

    /**
     * 项目所属分类名称
     */
    @TableField(value = "project_category_name")
    private String projectCategoryName;

    /**
     * 合同所属分类名称
     */
    @TableField(value = "contract_category_name")
    private String contractCategoryName;

    /**
     * 合同所属分类 A:货物 B：工程 C：服务
     */
    @TableField(value = "contract_category_code")
    private String contractCategoryCode;

    /**
     * 超期时间
     */
    @TableField(value = "overdue_time")
    private LocalDateTime overdueTime;
}
