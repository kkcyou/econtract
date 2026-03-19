package com.yaoan.module.econtract.dal.dataobject.gcy.gpmall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 订单的附件信息
 * @author: Pele
 * @date: 2023/11/29 11:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_file_attachment")
public class FileAttachmentDO extends BaseDO {

    private static final long serialVersionUID = -2166341090635560652L;
    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 附件名称
     */
    private String fileName;
    /**
     * 附件路径
     */
    private String path;

    /**
     * 订单id
     */
    private String orderId;
}
