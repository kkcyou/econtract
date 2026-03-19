package com.yaoan.module.econtract.dal.dataobject.gcy.gpmall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批注实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_comment")
public class CommentDO extends BaseDO {

    private static final long serialVersionUID = 0x939ebd28ea4fbeccL;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 备注名
     */
    private String name;

    /**
     * 备注编码
     */
    private String code;

    /**
     * 备注内容
     */
    private String content;

}
