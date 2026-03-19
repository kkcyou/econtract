package com.yaoan.module.econtract.dal.dataobject.code;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 编号规则详情
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_code_rule_info")
public class CodeRuleInfoDO extends TenantBaseDO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 编号规则id
     */
    private String codeRuleId;

    /**
     * 编号前缀
     */
    private String prefix;

    /**
     * 连接符
     */
    private String connector;

    /**
     * 固定文本
     */
    private String fixedText;

    /**
     *  上次重置日期
     */
    private LocalDate lastResetDate;

    /**
     * 下一个序列号
     */
    private Long nextSerialNumber;

    /**
     * 排序
     */
    private Integer sort;
}
