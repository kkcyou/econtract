package com.yaoan.module.econtract.controller.admin.version.vo.list;

import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/8/29 20:37
 */
@Data
public class FileVersionPageReqVO extends PageParam {
    /**
     * 业务id
     */
    private String businessId;
}
