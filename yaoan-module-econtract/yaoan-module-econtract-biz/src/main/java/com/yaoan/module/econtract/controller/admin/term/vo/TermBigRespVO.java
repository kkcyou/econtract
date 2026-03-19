package com.yaoan.module.econtract.controller.admin.term.vo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.FlowableConfigParamBaseRespVO;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/26 15:18
 */
@Data
public class TermBigRespVO extends FlowableConfigParamBaseRespVO {

    private static final long serialVersionUID = -5441615299521774239L;

    private PageResult<TermRespVO> pageResult;
}
