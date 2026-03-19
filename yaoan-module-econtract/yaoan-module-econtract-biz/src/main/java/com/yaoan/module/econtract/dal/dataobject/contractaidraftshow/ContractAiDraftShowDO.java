package com.yaoan.module.econtract.dal.dataobject.contractaidraftshow;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 合同模板推荐 DO
 *
 * @author doujiale
 */
@TableName("ecms_contract_ai_draft_show")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractAiDraftShowDO extends BaseDO {

    /**
     * 模板ID
     */
    @TableId
    private Long templateiId;
    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 使用的大模型
     */
    private String model;
    /**
     * 模板内容
     */
    private String templateContent;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 使用次数
     */
    private Integer useNum;

    /**
     * 文件id
     */
    private String fileId;
}
