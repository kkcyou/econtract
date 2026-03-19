package com.yaoan.module.econtract.controller.admin.outward.contract.VO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@ToString(callSuper = true)
public class ApiPageRespVO implements Serializable{
    private static final long serialVersionUID = 4647397149382580379L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同编码
     */
    private String code;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 签署状态
     * 0-待发送 - 新增
     * 1-被退回
     * 2-已发送
     * 3-待确认
     * 4-待签署
     * 5-已拒签
     * 6-签署完成
     * 7-逾期未签署
     * 8-合同终止签署中
     * 9-合同终止
     * 10-合同变更
     * 11-待送审
     * 12-审核中
     * 13审核未通过
     */
    private Integer status;

    /**
     * 合同金额
     */
    private Double amount;

    /**
     * 合同有效期-开始时间
     */
    private Date effectDate;

    /**
     * 合同有效期-结束时间
     */
    private Date expiryDate;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
