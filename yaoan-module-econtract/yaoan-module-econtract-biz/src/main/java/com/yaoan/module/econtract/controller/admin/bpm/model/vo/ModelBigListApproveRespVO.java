package com.yaoan.module.econtract.controller.admin.bpm.model.vo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.FlowableConfigParamBaseRespVO;
import lombok.Data;

@Data
public class ModelBigListApproveRespVO extends FlowableConfigParamBaseRespVO {

    private static final long serialVersionUID = 1038580331372481703L;

    /**
     * 列表信息
     */
    private PageResult<ModelListApproveRespVO> pageResult;


}