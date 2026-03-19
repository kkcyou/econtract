package com.yaoan.module.econtract.dal.dataobject.reviewresult;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 智能审查结果 DO
 *
 * @author admin
 */
@TableName("ecms_review_result")
@KeySequence("ecms_review_result_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResultDO extends BaseDO {

    private static final long serialVersionUID = 5956291264343796736L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 审查结果（0=通过，1=低风险，2=中风险，3=高风险）
     * {@link com.yaoan.module.econtract.enums.contractreviewitems.RiskLevelEnums}
     */
    private Integer result;
    /**
     * 标题
     */
    private String title;
    /**
     * 风险等级（1=低风险，2=中风险，3=高风险）
     */
    private Integer riskLevel;
    /**
     * 风险提示
     */
    private String riskWarning;
    /**
     * 版本
     */
    private Long version;

}
