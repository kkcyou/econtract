package com.yaoan.module.econtract.controller.admin.amount.vo.alert;

import cn.hutool.core.date.DateTime;
import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/28 16:12
 */
@Data
public class GPMallAlertListReqVO extends PageParam {

    /**
     * 时间start
     */
    private DateTime time0;
    /**
     * 时间end
     */
    private DateTime time1;
}