package com.yaoan.module.econtract.api.ledger.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "台账DTO对象", description = "台账DTO对象")
public class LedgerDTO extends PageParam implements Serializable {

    private static final long serialVersionUID = -3044169451455706471L;

    /**
     * 台账id
     */
    @Schema(name = "id", title = "台账id")
    @NotBlank(message = "台账id不能为空")
    private String id;

    /**
     * 合同编号
     */
    private String code;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 合同类型（all=全部、sales=销售合同、purchase=采购合同、rental =租赁合同、transportation =运输合同、warehousing =仓储合同 等）
     */
    private String contractType;

    /**
     * 合同状态（all=全部，signing=签署中，signed=签署完成，unsigned=签署未完成，overdue_unsigned=逾期未签署、voiding=作废中、voided=已作废、terminating=终止中、terminated=已终止、filed=已归档、fufilling=履约中、overdue_fufilling=履约超期、pause_fulfil=履约暂停、finished=合同完成、overdue_finished=合同超期完成）
     */
    private String contractStatus;

    /**
     * 合同金额
     */
    private BigDecimal contractFinance;

    /**
     * 我方签约主体
     */
    private String myContractParty;

    /**
     * 相对方签约主体
     */
    private String counterparty;

    /**
     * 合同签订时间
     */
    private LocalDateTime signTime;

    /**
     * 合同归档时间
     */
    private LocalDateTime filingTime;

    /**
     * 合同签署方式（all=全部，scan=扫码签、ukey=Ukey签署）
     */
    private String signType;

    /**
     * 合同用章类型(all=全部，contract =合同章、official =公章、corporate =法人章
     */
    private String sealingType;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 0=没删除，1=已删除
     */
    private Boolean deleted;

    /**
     * 上传文件访问路径
     */
    private String uploadFilePath;

}
