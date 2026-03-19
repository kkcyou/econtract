package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yaoan.framework.excel.core.annotations.DictFormat;
import com.yaoan.framework.excel.core.convert.DictConvert;
import lombok.Data;


/**
 * @author wsh
 */
@Data
public class ContractReviewItemsExcelVO {

    @ExcelProperty("审查内容")
    private String reviewContent;

    @ExcelProperty("所属条款")
    private Integer termId;

    @ExcelProperty(value = "适用范围(适用的合同分类)", converter = DictConvert.class)
    @DictFormat("contract_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private String contractTypes;

    @ExcelProperty(value = "风险不利方", converter = DictConvert.class)
    @DictFormat("risk_party_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer riskParty;

    @ExcelProperty(value = "风险等级", converter = DictConvert.class)
    @DictFormat("risk_level") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer riskLevel;

}
