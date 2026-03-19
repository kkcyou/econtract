package com.yaoan.module.econtract.controller.admin.formconfig.vo;

import com.yaoan.module.econtract.dal.dataobject.formconfig.FormItemDO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/18 19:35
 */
@Data
public class FormConfigSaveReqVO {
    /**
     * 业务id
     */
    private String businessId;

    /**
     * 业务名称
     */
    private String name;

    /**
     * 表单集合
     */
    private List<FormReqVO> formReqVOS;


}
