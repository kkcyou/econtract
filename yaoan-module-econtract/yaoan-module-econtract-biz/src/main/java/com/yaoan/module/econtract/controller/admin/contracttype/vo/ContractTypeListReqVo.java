package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/23 13:38
 */
@Data
@Schema(description = "合同类型")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractTypeListReqVo extends PageParam {

    /**
     * 合同类型名称/创建者
     */
    @Schema(description = "合同类型名称/创建者", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String searchText;

    /**
     * 合同类型分类
     */
    @Schema(description = "合同类型分类id", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer typeCategory;


    /**
     * 创建时间起始（查询范围）
     */
    @Schema(description = "创建时间起始（查询范围）", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startCreateTime;

    /**
     * 创建时间结束（查询范围）
     */
    @Schema(description = "创建时间结束（查询范围）", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endCreateTime;
}
