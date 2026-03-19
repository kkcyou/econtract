package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.yaoan.framework.common.pojo.PageResult;
import lombok.Data;


@Data
public class SealApplicationListBpmRespVO  {
    /**
     * 列表信息
     */
    private PageResult<SignetProcessPageRespVO> pageResult;
}
