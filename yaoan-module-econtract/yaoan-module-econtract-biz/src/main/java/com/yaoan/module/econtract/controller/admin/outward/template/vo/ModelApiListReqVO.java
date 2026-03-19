package com.yaoan.module.econtract.controller.admin.outward.template.vo;

import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/4 10:18
 */
@Validated
@Data
public class ModelApiListReqVO extends PageParam {

    /**
     * 模板所属区划code
     */
    private String regionCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板分类id
     */
    private String categoryId;

    /**
     * 模板分类id
     */
    private List<Integer> categoryIdList;

}
