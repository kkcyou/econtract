package com.yaoan.module.econtract.dal.dataobject.review;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("ecms_review_config_log")
public class ReviewConfigLogDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 5908910199869108843L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同类型id
     */
    private String typeId;

    /**
     * 角色
     */
    private String role;

    /**
     * 名称
     */
    private String name;

    /**
     * 审查名称旧
     */
    private String reviewName0;

    /**
     * 审查名称新
     */
    private String reviewName1;


}

