package com.yaoan.module.econtract.dal.dataobject.contracttemplate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @description: 参考模板和条款关系表
 * @author: Pele
 * @date: 2024/4/29 16:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "ecms_contract_template_term_rel")
public class ContractTemplateTermRelDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 5649800129054339297L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 范本id
     */
    private String templateId;

    /**
     * 条款Id
     */
    private String termId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 条款在该范本里的标题
     */
    private String title;
}
