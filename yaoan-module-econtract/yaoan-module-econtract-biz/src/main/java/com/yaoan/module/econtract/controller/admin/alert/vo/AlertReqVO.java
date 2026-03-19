package com.yaoan.module.econtract.controller.admin.alert.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2023/11/8 15:09
 */
@Data
public class AlertReqVO extends PageParam {

    @Schema(description = "模版内容-富文本格式")
    private String modelContent = "";
}
