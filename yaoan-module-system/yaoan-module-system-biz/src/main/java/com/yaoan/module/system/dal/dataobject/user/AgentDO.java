package com.yaoan.module.system.dal.dataobject.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
@TableName("system_agent")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentDO extends BaseDO {
    private String id;
    private String agentName;
    private String agentAbbrname;
    private String businessAddress;
    private String industryCode;
    private String agentCode;
    private Integer category;
    private String agentAbbr;
    private String nature;
    private String province;
    private String city;
    private String county;
    private String road;
    private String zipCode;
    private String fixedTel;
    private String email;
    private String basicBankAccount;
    private String basicBankName;
    private String legalPersonName;
    private String legalIdcard;
    private String legalZipCode;
    private String legalTel;
    private String legalIdcardAttach;
    private String busPersonName;
    private String busTel;
    private String busFaxNo;
    private String busZipCode;
    private String licCredNum;
    private BigDecimal regCapital;
    private Date issueLicTime;
    private String issueLicAgency;
    private String businessScope;
    private String licAttach;
    private String reviewPlaceAddr;
    private BigDecimal reviewPlaceForests;
    private Integer fulltimeNum;
    private Integer isLawTax;
    private Integer isLawInsurance;
    private Integer secPosTitleNum;
    private BigDecimal secPosTitilePercent;
    private Integer purchasePersonNum;
    private BigDecimal purchasePersonPercent;
    private Integer haveIllegalRecord;
    private String illegalContent;
    private Integer kindCode;
    private String status;
}
