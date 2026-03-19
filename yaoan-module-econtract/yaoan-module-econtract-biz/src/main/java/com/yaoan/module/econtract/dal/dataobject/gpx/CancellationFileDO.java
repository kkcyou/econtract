package com.yaoan.module.econtract.dal.dataobject.gpx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @date: 2024/6/29 14:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_cancellation")
public class CancellationFileDO extends BaseDO {
    private static final long serialVersionUID = 7074013236436766783L;

    @TableId(value = "id" ,type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 附件id
     */
    private Long fileId;

    /**
     * 退回理由
     */
    private String reason;
}
