package com.yaoan.module.econtract.controller.admin.copyrecipient.vo;

import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/1 22:39
 */
@Data
public class CopyRecipientPageReqVO extends PageParam {

    /**
     * 流程定义key
     */
    private String processDefinitionKey;
}
