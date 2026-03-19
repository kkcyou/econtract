package com.yaoan.module.econtract.dal.dataobject.review;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("ecms_review_data")
public class PointsDataDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 671760933780658522L;
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
    /**
     * 风险提示语
     */
    private String riskPrompt;


}

