package com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025/1/10 10:48
 */
@Data
public class BigGPMallPageRespVO {

    /**
     * 是否合并订单
     * {@link com.yaoan.module.econtract.enums.common.IfEnums}
     * */
    private String ifMergeOrder;

    /**
     * 合并订单的分页
     */
    private PageResult<GroupedDraftOrderInfoVO> mergePageResult;

    /**
     * 非合并订单的分页
     */
    private PageResult<GPMallPageRespVO> unmergePageResult;
}
