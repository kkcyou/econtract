package com.yaoan.module.econtract.controller.admin.rtf.vo;

import com.yaoan.module.econtract.controller.admin.model.vo.TermsDetailsVo;
import lombok.Data;

import java.util.List;

@Data
public class ModelRespVO {
    /**
     * 富文本
     */
    private String content;


    /**
     * 条款列表
     */
    private List<TermsDetailsVo> terms;
}
