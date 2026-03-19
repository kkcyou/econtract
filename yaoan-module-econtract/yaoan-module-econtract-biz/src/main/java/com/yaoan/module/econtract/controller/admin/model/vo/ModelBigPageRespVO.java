package com.yaoan.module.econtract.controller.admin.model.vo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.FlowableConfigParamBaseRespVO;
import lombok.Data;

@Data
public class ModelBigPageRespVO extends FlowableConfigParamBaseRespVO {
    private static final long serialVersionUID = -4373084293724093823L;

    private PageResult<ModelPageRespVO> pageResult;
}
