package com.yaoan.module.econtract.api.purchasing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 批量删除和校验是否有调用信息使用
 */
@Data
public class ReqIdsDTO {

    @Schema(description = "ids列表")
    private List<String> ids;

    @Schema(description = "id")
    private String id;

    @Schema(description = "类型：项目采购id为1，框架协议为2，电子卖场为3")
    private Integer type;

}
