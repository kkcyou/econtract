package com.yaoan.module.econtract.controller.admin.workbench.v2.vo;

import com.yaoan.module.econtract.enums.contract.ContractSourceTypeEnums;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2025/2/11 10:25
 */
@Data
public class ContractDataRespVO {

    /**
     * 合同id
     */
    private String id;

    /**
     * 合同名称
     */
    private String name;
    /**
     * 合同编号
     */
    private String code;
    /**
     * 合同金额
     */
    private Double amount;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * {@link com.yaoan.module.econtract.enums.ContractStatusEnums}
     */
    private Integer status;
    /**
     * {@link com.yaoan.module.econtract.enums.ContractStatusEnums}
     */
    private String statusName;

    private String supplierId;

    private String supplierName;

    /**
     * 付款进度
     * */
    private  BigDecimal period;

    /**
     * 补充
     */
    private String remark;

    /**
     * {@link com.yaoan.module.econtract.enums.ContractUploadTypeEnums}
     * */
    private Integer upload;

    /**
     * {@link ContractSourceTypeEnums}
     * */
    private Integer contractSourceType;

    private String platform;

    private String biddingMethodCode;

    /**
     * 项目类型
     */
    private String projectType;

    /**
     * contract,seal,pay,collection
     */
    @NotNull(message = "flag不可为空")
    private String flag;
    /**
     * 乙方名称
     */
    private String partBName;

    /**
     * 相对方流程的规则设置（0=会签，1=依次签）
     * */
    private String flowSortRule;
}
