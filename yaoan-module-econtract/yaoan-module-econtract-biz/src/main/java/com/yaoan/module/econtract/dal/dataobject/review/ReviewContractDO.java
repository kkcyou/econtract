package com.yaoan.module.econtract.dal.dataobject.review;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("ecms_review_contract_rel")
public class ReviewContractDO extends BaseDO implements Serializable{

    private static final long serialVersionUID = 5011313262243511524L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 审查清单id
     */
    private String reviewId;

    /**
     * 合同类型id
     */
    private String typeId;

    /**
     * 合同类型名称
     */
    private String typeName;



}
