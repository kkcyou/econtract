package com.yaoan.module.econtract.dal.dataobject.copyrecipients;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/1 19:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_bpm_copy_recipients")
public class BpmCopyRecipientsDO extends DeptBaseDO {

    private static final long serialVersionUID = -4394196030339037108L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 抄送人的用户编号
     */
    @TableField("recipient_id")
    private Long recipientId;

    /**
     * 流程定义id
     */
    @TableField("process_definition_key")
    private String processDefinitionKey;

    /**
     * 业务id
     */
    @TableField("business_id")
    private String businessId;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 审批结果
     */
    private Integer result;

}
