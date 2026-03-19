package com.yaoan.module.econtract.controller.admin.supervise.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @description:
 * @author: ZHC
 * @date: 2023/11/28 11:46
 */
@Data
public class QueryBuyPlanBilReplVO extends PageParam {


    /**
     * 分包的唯一识别码
     */
    @Schema(description = "分包的唯一识别码")
    private String bidGuid;

    /**
     * 搜索框字符串
     */
    @Schema(description = "搜索框字符串")
    private String searchText;

}
