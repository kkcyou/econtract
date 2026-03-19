package com.yaoan.module.econtract.controller.admin.warningitemrule.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.module.econtract.enums.warning.WarningCompareTypeEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 预警规则（new预警）创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarningItemRuleCreateReqVO extends WarningItemRuleBaseVO {

    /**
     * 预警事项id
     */
    private String warningItemId;
    /**
     * 预警事项名称
     */
    private String warningItemName;
    /**
     * 监控项id
     */
    private String monitorItemId;
    /**
     * 监控项名称
     */
    private String monitorItemName;
    /**
     * 比较类型（大于小于等于范围不等于立即执行）
     * {@link WarningCompareTypeEnum}
     */
    private Integer compareType;
    /**
     * 比较项1/阈值（整型）
     */
    private Integer compareItemStart;
    /**
     * 比较项2/阈值  (整型)
     */
    private Integer compareItemEnd;
    /**
     * 比较项1（浮点型，为金额和百分比预留）
     */
    private BigDecimal compareDecItemStart;
    /**
     * 比较项2（浮点型，为金额和百分比预留）
     */
    private BigDecimal compareDecItemEnd;
    /**
     * 比较项1（日期类型，预留）
     */
    private LocalDateTime compareDateItemStart;
    /**
     * 比较项2（日期类型，预留）
     */
    private LocalDateTime compareDateItemEnd;
    /**
     * 阈值单位（自然日，工作日，金额，数量，百分比）
     */
    private Integer compareDataType;
}
