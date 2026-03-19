package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/23 20:00
 */
@Data
public class PaymentPlanConfirmReqVO {

    /**
     *  收款/付款申请主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    
    /**
     * 收款/付款确认备注  
     */
    private String confirmRemark;

    private List<BusinessFileDO> confirmFileList;
    private LocalDateTime confirmTime;
}
