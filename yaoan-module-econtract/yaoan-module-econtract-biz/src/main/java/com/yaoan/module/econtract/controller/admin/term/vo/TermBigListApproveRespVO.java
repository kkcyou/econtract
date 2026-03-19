package com.yaoan.module.econtract.controller.admin.term.vo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.term.vo.bpm.TermListApproveRespVO;
import lombok.Data;

@Data
public class TermBigListApproveRespVO extends FlowableConfigParamBaseRespVO {
    private static final long serialVersionUID = 83574112431051502L;

    private PageResult<TermListApproveRespVO> pageResult;
}
