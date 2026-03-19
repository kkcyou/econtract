package com.yaoan.module.econtract.controller.admin.bpm.common;

import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableConfigRespVO;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableParam;
import lombok.Data;

import java.io.Serializable;

@Data
public class FlowableConfigParamBaseRespVO  implements Serializable {

    private static final long serialVersionUID = 8689272866469947004L;

    /**
     * 工作流配置信息
     */
    private FlowableConfigRespVO flowableConfigRespVO;
}
