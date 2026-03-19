package com.yaoan.module.econtract.dal.dataobject.contractaidraftrecord;

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
 * 合同智能起草记录 DO
 *
 * @author doujiale
 */
@TableName("ecms_contract_ai_draft_record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractAiDraftRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 标题
     */
    private String title;

    /**
     * 使用的大模型
     */
    private String llmName;
    /**
     * 文件id
     */
    private String fileId;

    /**
     * 快照状态（0需求填写、1生成中、2已生成）
     */
    private String snapshotStatus;
    /**
     * 完整快照内容
     */
    private String snapshot;
    /**
     * 关联的合同ID
     */
    private String contractId;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同类型（如：采购合同服务合同）
     */
    private String contractType;
    /**
     * 模板信息列表（JSON数组）
     */
    private String templateInfo;
    /**
     * 模板信息列表（JSON数组）
     */
    private String templateInfoShow;

    /**
     * 生成的合同内容
     */
    private String contractContent;

    /**
     * 合同生成方式
     */
    private String contractGenerateType;

    /**
     * 选择的模板
     */
    private String templateSelect;

}
