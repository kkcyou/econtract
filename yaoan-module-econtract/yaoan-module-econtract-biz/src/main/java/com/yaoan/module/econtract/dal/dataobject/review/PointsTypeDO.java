package com.yaoan.module.econtract.dal.dataobject.review;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("ecms_review_type")
public class PointsTypeDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 2447232209491411662L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 审查点名称
     */
    private String name;
    /**
     * 父类id
     */
    private String parentId;

}

