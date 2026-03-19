package com.yaoan.module.econtract.controller.admin.relative.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author doujiale
 */
@Data
@Schema(description = "新增相对方信息")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RelativeCreateReqVO extends BaseRelativeVO {

    /**
     * 相对方公司id
     */
    private Long relativeCompanyId;



}
