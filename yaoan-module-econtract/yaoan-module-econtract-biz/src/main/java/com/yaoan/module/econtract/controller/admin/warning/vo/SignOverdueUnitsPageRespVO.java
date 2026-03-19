package com.yaoan.module.econtract.controller.admin.warning.vo;
import com.yaoan.framework.common.pojo.PageResult;
import lombok.Data;

@Data
public class SignOverdueUnitsPageRespVO {


    /**
     * 超期已签订数量
     */
    private Long signedOverdueCount;
    /**
     * 超期未签订数量
     */
    private Long unSignedOverdueCount;

    private PageResult<SignOverdueUnitsListRespVO> pageResult;

}
