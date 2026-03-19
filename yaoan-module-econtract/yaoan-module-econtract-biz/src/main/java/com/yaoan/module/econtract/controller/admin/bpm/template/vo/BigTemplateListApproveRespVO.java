package com.yaoan.module.econtract.controller.admin.bpm.template.vo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.FlowableConfigParamBaseRespVO;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/13 11:31
 */
@Data
public class BigTemplateListApproveRespVO extends FlowableConfigParamBaseRespVO {
    private static final long serialVersionUID = 3379655131027092142L;
    private PageResult<TemplateListApproveRespVO> pageResult;
}
